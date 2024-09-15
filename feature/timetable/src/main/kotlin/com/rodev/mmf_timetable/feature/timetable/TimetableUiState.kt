package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Weekday

sealed interface TimetableUiState {

    @Immutable
    data class Timetable(
        val currentStudyWeek: Long? = null,
        val timetable: Map<Weekday, Timetable> = emptyMap(),
        val weekdays: List<Weekday> = emptyList(), // TODO
        val todayWeekday: Weekday = Weekday.FRIDAY // TODO
    ) : TimetableUiState

    object Loading : TimetableUiState

    data class Error(val exception: Throwable) : TimetableUiState

    object CourseNotSelected : TimetableUiState

}