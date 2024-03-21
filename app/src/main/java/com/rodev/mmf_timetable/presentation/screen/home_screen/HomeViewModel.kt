package com.rodev.mmf_timetable.presentation.screen.home_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.K
import com.rodev.mmf_timetable.domain.mapper.LessonMapper
import com.rodev.mmf_timetable.domain.model.Timetable
import com.rodev.mmf_timetable.domain.model.UserInfo
import com.rodev.mmf_timetable.domain.service.ApiResult
import com.rodev.mmf_timetable.domain.use_case.GetCoursesUseCase
import com.rodev.mmf_timetable.domain.use_case.GetCurrentWeekUseCase
import com.rodev.mmf_timetable.domain.use_case.GetTimetableUseCase
import com.rodev.mmf_timetable.domain.use_case.IsLessonAvailableUseCase
import com.rodev.mmf_timetable.domain.use_case.LoadUserInfoUseCase
import com.rodev.mmf_timetable.domain.use_case.SaveUserInfoUseCase
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.AvailableLesson
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.HomeScreenEvent
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.HomeScreenResult
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCourses: GetCoursesUseCase,
    private val getTimetable: GetTimetableUseCase,
    private val saveUserInfo: SaveUserInfoUseCase,
    private val getUserInfo: LoadUserInfoUseCase,
    private val isLessonAvailable: IsLessonAvailableUseCase,
    private val getCurrentWeek: GetCurrentWeekUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(
        HomeScreenState(
            courseList = getCourses(),
            userInfo = getUserInfo()
        )
    )

    val state = _state.asStateFlow()

    private var fetchJob: Job? = null

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.FetchTimetable -> {
                val state = _state.value

                val course = state.userInfo?.course
                val group = state.userInfo?.group

                if (course == null || group == null) return

                updateState {
                    it.copy(
                        result = HomeScreenResult.Loading
                    )
                }

                fetchJob?.cancel()
                fetchJob = viewModelScope.launch(Dispatchers.IO) {
                    try {
                        onFetchTimetable(course.course, group.id)
                    } catch (e: Throwable) {
                        emitError(e.message.toString())
                    }
                }
            }

            HomeScreenEvent.OpenCourseEditDialog -> {
                updateState {
                    it.copy(
                        courseEditDialogOpened = true
                    )
                }
            }

            HomeScreenEvent.CloseCourseEditDialog -> {
                updateState {
                    it.copy(
                        courseEditDialogOpened = false
                    )
                }
            }

            is HomeScreenEvent.CourseAndGroupSelected -> {
                val course = event.course
                val group = event.group
                val subGroup = event.subGroup

                saveUserInfo(course, group, subGroup)

                updateState {
                    it.copy(
                        courseEditDialogOpened = false,
                        userInfo = UserInfo(
                            course = course,
                            group = group,
                            subGroup = subGroup
                        )
                    )
                }
            }
        }
    }

    private suspend fun onFetchTimetable(course: Int, group: String) {
        when (val result = getTimetable(course, group)) {
            is ApiResult.Exception -> {
                emitError(result.exception.message.toString())
            }
            is ApiResult.Failure -> {
                emitError(result.error)
            }
            is ApiResult.Success -> {
                updateState {
                    it.copy(
                        result = HomeScreenResult.Idle,
                        timetable = LessonMapper.mapLessonsByWeekday(result.data.allLessons) { lesson ->
                            AvailableLesson(
                                wrappedLesson = lesson,
                                available = isLessonAvailable(lesson)
                            )
                        },
                        currentWeek = getCurrentWeek()
                    )
                }
            }
        }
    }

    private fun emitError(message: String) {
        updateState {
            it.copy(
                result = HomeScreenResult.Error(message = message, onConsume = ::consumeError)
            )
        }
    }

    private fun consumeError() {
        updateState {
            it.copy(
                result = HomeScreenResult.Idle
            )
        }
    }

    private inline fun updateState(updater: (HomeScreenState) -> HomeScreenState) {
        _state.update(updater)
    }

}