package com.rodev.mmf_timetable.presentation.screen.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.domain.repository.TimetableRepository
import com.rodev.mmf_timetable.domain.repository.UserDataRepository
import com.rodev.mmf_timetable.core.result.Resource
import com.rodev.mmf_timetable.core.result.asResource
import com.rodev.mmf_timetable.presentation.screen.timetable.state.LessonUiState
import com.rodev.mmf_timetable.presentation.screen.timetable.state.TimetableUiState
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
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    timetableRepository: TimetableRepository,
) : ViewModel() {
    val state = timetableUiState(
        userDataRepository,
        timetableRepository
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
    timetableRepository: TimetableRepository
): Flow<TimetableUiState> {
    return userDataRepository.userData.distinctUntilChanged().flatMapLatest { userData ->
        if (userData == null) {
            return@flatMapLatest flowOf(TimetableUiState.CourseNotSelected)
        }

        // TODO rework
        timetableRepository
            .getTimetableStream(userData.course, userData.groupId)
            .mapLatest {
                when {
                    it == null -> {
                        timetableRepository.refresh(userData.course, userData.groupId)
                        null
                    }
                    it.dirty -> try {
                        timetableRepository.refresh(userData.course, userData.groupId)
                        null
                    } catch (e: Exception) {
                        it
                    }
                    else -> it
                }
            }
            .asResource()
            .map { resource ->
                when (resource) {
                    com.rodev.mmf_timetable.core.result.Resource.Loading -> TimetableUiState.Loading
                    is com.rodev.mmf_timetable.core.result.Resource.Exception -> TimetableUiState.Error(resource.exception)
                    is com.rodev.mmf_timetable.core.result.Resource.Success -> {
                        val data = resource.data

                        if (data !== null) {
                            TimetableUiState.Success(
                                currentStudyWeek = data.week.toLong(),
                                timetable = data.lessons.mapValues { (_, list) ->
                                    list.map { LessonUiState(wrappedLesson = it, available = true) }
                                }
                            )
                        } else {
                            TimetableUiState.Loading
                        }
                    }
                }
            }
    }
}