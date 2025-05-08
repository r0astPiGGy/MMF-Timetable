package com.rodev.mmf_timetable.feature.timetable

import android.util.Log.i
import android.util.Log.w
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import com.rodev.mmf_timetable.core.domain.GetAvailableLessonsUseCase.Companion.isAvailable
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import com.rodev.mmf_timetable.feature.timetable.model.DateWeekday
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
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

private fun weekOf(date: LocalDate): List<DateWeekday> {
    // 0 - 6
    val offset = date.dayOfWeek.value - 1
    return listOf(
        Weekday.MONDAY,
        Weekday.TUESDAY,
        Weekday.WEDNESDAY,
        Weekday.THURSDAY,
        Weekday.FRIDAY,
        Weekday.SATURDAY,
        Weekday.SUNDAY
    ).mapIndexed { i, week ->
        return@mapIndexed DateWeekday(
            date = date.plus(i - offset, DateTimeUnit.DAY),
            weekday = week
        )
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
                                    }.sortedBy { it.lesson.timeStartMinutes }
                                }

                            TimetableUiState.Timetable(
                                timetable = timetable,
                                week = week,
                                selectedDate = selectedDate
                            )
                        }
                    }
                }
        }
}