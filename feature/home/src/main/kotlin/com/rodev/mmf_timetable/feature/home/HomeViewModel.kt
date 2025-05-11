package com.rodev.mmf_timetable.feature.home

import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.core.data.repository.CourseRepository
import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    lessonRepository: LessonRepository,
    coursesRepository: CourseRepository
) : ViewModel() {
    val state = homeUiState(
        userDataRepository,
        lessonRepository,
        coursesRepository
    )
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun homeUiState(
    userDataRepository: UserDataRepository,
    lessonRepository: LessonRepository,
    courseRepository: CourseRepository
): Flow<HomeUiState> {
    return userDataRepository.userData
        .distinctUntilChanged()
        .flatMapLatest { userData ->
            if (userData == null) {
                return@flatMapLatest flowOf(HomeUiState.CourseNotSelected)
            }

            return@flatMapLatest lessonRepository.getLessons(userData.groupId)
                .combine(courseRepository.getCourses()) { lessons, courses ->
                    val selectedGroup = courses.flatMap { it.groups }.firstOrNull { it.id == userData.groupId }

                    lessons to selectedGroup
                }
                .asResult()
                .map { resource ->
                    when (resource) {
                        is Result.Exception -> HomeUiState.Error(resource.exception)
                        Result.Loading -> HomeUiState.Loading
                        is Result.Success -> {
                            HomeUiState.Home(
                                currentLesson = null,
                                group = resource.data.second
                            )
                        }
                    }
                }
        }
}