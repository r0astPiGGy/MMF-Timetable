package com.rodev.mmf_timetable.presentation.screen.timetable.state

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.utils.DateUtils

typealias MappedTimetable = Map<com.rodev.mmf_timetable.core.model.data.Weekday, List<LessonUiState>>

sealed interface TimetableUiState {

    @Immutable
    data class Success(
        val currentStudyWeek: Long? = null,
        val timetable: MappedTimetable = emptyMap(),
        val weekdays: List<com.rodev.mmf_timetable.core.model.data.Weekday> = timetable.provideWeekdays(),
        val todayWeekday: com.rodev.mmf_timetable.core.model.data.Weekday = DateUtils.getCurrentWeekday()
    ) : TimetableUiState

    object Loading : TimetableUiState

    data class Error(val exception: Throwable) : TimetableUiState

    object CourseNotSelected : TimetableUiState

}

private val allWeekdays = arrayOf(
    com.rodev.mmf_timetable.core.model.data.Weekday.MONDAY,
    com.rodev.mmf_timetable.core.model.data.Weekday.TUESDAY,
    com.rodev.mmf_timetable.core.model.data.Weekday.WEDNESDAY,
    com.rodev.mmf_timetable.core.model.data.Weekday.THURSDAY,
    com.rodev.mmf_timetable.core.model.data.Weekday.FRIDAY,
    com.rodev.mmf_timetable.core.model.data.Weekday.SATURDAY
)

fun MappedTimetable?.provideWeekdays(): List<com.rodev.mmf_timetable.core.model.data.Weekday> {
    if (this == null) return emptyList()

    return allWeekdays.filter { containsKey(it) }
}