package com.rodev.mmf_timetable.feature.timetable

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import com.rodev.mmf_timetable.core.domain.getCurrentLessonIn
import com.rodev.mmf_timetable.core.domain.isAvailable
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import com.rodev.mmf_timetable.core.ui.weekOf
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
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    userDataRepository: UserDataRepository,
    lessonRepository: LessonRepository,
) : ViewModel() {
    private val selectedDate = savedStateHandle.getStateFlow("date", now().toEpochDays())
        .map(LocalDate::fromEpochDays)

    val state = timetableUiState(
        userDataRepository,
        lessonRepository,
        selectedDate
    )
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimetableUiState.Loading
        )

    fun selectDate(date: LocalDate) {
        savedStateHandle["date"] = date.toEpochDays()
    }

    private fun now(): LocalDate {
        return Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
private fun timetableUiState(
    userDataRepository: UserDataRepository,
    lessonRepository: LessonRepository,
    selectedDateFlow: Flow<LocalDate>
): Flow<TimetableUiState> {
    return userDataRepository.userData
        .distinctUntilChanged()
        .flatMapLatest { userData ->
            if (userData == null) {
                return@flatMapLatest flowOf(TimetableUiState.CourseNotSelected)
            }

            return@flatMapLatest lessonRepository.getLessons(userData.groupId)
                .combine(selectedDateFlow) { timetable, date -> timetable to date }
                .asResult()
                .map { resource ->
                    when (resource) {
                        is Result.Exception -> TimetableUiState.Error(resource.exception)
                        Result.Loading -> TimetableUiState.Loading
                        is Result.Success -> {
                            val (lessons, date) = resource.data

                            val week = weekOf(date)
                            val selectedDate = week.first { it.date == date }

                            val timetable = lessons.groupBy { l -> week.first { it.weekday ==  l.weekday } }
                                .mapValues { (k, v) ->
                                    v.map { l ->
                                        AvailableLesson(
                                            isAvailable = l.isAvailable(
                                                date = k.date,
                                                subGroups = userData.subgroups
                                            ),
                                            lesson = l
                                        )
                                    }.sortedBy { it.lesson.timeStart }
                                }

                            TimetableUiState.Timetable(
                                timetable = timetable,
                                week = week,
                                selectedDate = selectedDate,
                                currentLesson = getCurrentLessonIn(lessons, subGroups = userData.subgroups)
                            )
                        }
                    }
                }
        }
}