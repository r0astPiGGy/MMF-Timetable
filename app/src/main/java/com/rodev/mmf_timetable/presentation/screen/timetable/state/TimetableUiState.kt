package com.rodev.mmf_timetable.presentation.screen.timetable.state

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.domain.model.Weekday
import com.rodev.mmf_timetable.utils.DateUtils

typealias MappedTimetable = Map<Weekday, List<LessonUiState>>

sealed interface TimetableUiState {

    @Immutable
    data class Success(
        val currentStudyWeek: Long? = null,
        val timetable: MappedTimetable = emptyMap(),
        val weekdays: List<Weekday> = timetable.provideWeekdays(),
        val todayWeekday: Weekday = DateUtils.getCurrentWeekday()
    ) : TimetableUiState

    object Loading : TimetableUiState

    data class Error(val exception: Throwable) : TimetableUiState

    object CourseNotSelected : TimetableUiState

}

private val allWeekdays = arrayOf(
    Weekday.MONDAY,
    Weekday.TUESDAY,
    Weekday.WEDNESDAY,
    Weekday.THURSDAY,
    Weekday.FRIDAY,
    Weekday.SATURDAY
)

fun MappedTimetable?.provideWeekdays(): List<Weekday> {
    if (this == null) return emptyList()

    return allWeekdays.filter { containsKey(it) }
}