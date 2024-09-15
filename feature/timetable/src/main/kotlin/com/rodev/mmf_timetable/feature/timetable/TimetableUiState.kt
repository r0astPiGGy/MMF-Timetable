package com.rodev.mmf_timetable.feature.timetable

import android.text.format.DateUtils
import androidx.compose.runtime.Immutable

sealed interface TimetableUiState {

    @Immutable
    data class Timetable(
        val currentStudyWeek: Long? = null,
        val timetable: MappedTimetable = emptyMap(),
        val weekdays: List<com.rodev.mmf_timetable.core.model.data.Weekday> = timetable.provideWeekdays(),
        val todayWeekday: com.rodev.mmf_timetable.core.model.data.Weekday = DateUtils.getCurrentWeekday()
    ) : TimetableUiState

    object Loading : TimetableUiState

    data class Error(val exception: Throwable) : TimetableUiState

    object CourseNotSelected : TimetableUiState

}