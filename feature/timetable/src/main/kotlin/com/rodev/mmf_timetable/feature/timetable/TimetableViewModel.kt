package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    lessonRepository: LessonRepository,
) : ViewModel() {
    val state = timetableUiState(
        userDataRepository,
        lessonRepository
    )
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimetableUiState.Loading
        )
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun timetableUiState(
    userDataRepository: UserDataRepository,
    lessonRepository: LessonRepository
): Flow<TimetableUiState> {
    return userDataRepository.userData.distinctUntilChanged().flatMapLatest { userData ->
        if (userData == null) {
            return@flatMapLatest flowOf(TimetableUiState.CourseNotSelected)
        }

        return@flatMapLatest lessonRepository.getLessons(userData.groupId)
            .asResult()
            .map { resource ->
                when (resource) {
                    is Result.Exception -> TimetableUiState.Error(resource.exception)
                    Result.Loading -> TimetableUiState.Loading
                    is Result.Success -> {
                        val timetable = resource.data
                            .groupBy { it.weekday }
                            .mapValues { it.value.sortedBy { it.timeStartMinutes } }

                        TimetableUiState.Timetable(
                            timetable = timetable,
                            weekdays = timetable.keys.toList().sortedBy { it.ordinal },
                            todayWeekday = Weekday.MONDAY
                        )
                    }
                }
            }
        // TODO rework
//        lessonRepository
//            .getTimetableStream(userData.course, userData.groupId)
//            .mapLatest {
//                when {
//                    it == null -> {
//                        lessonRepository.refresh(userData.course, userData.groupId)
//                        null
//                    }
//                    it.dirty -> try {
//                        lessonRepository.refresh(userData.course, userData.groupId)
//                        null
//                    } catch (e: Exception) {
//                        it
//                    }
//                    else -> it
//                }
//            }
//            .asResult()
//            .map { resource ->
//                when (resource) {
//                    Result.Loading -> TimetableUiState.Loading
//                    is Result.Exception -> TimetableUiState.Error(resource.exception)
//                    is Result.Success -> {
//                        val data = resource.data
//
//                        if (data !== null) {
//                            TimetableUiState.Timetable(
//                                currentStudyWeek = data.week.toLong(),
//                                timetable = data.lessons.mapValues { (_, list) ->
//                                    list.map { LessonUiState(wrappedLesson = it, available = true) }
//                                }
//                            )
//                        } else {
//                            TimetableUiState.Loading
//                        }
//                    }
//                }
//            }
    }
}