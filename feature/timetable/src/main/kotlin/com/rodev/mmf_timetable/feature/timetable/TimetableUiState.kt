package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.Weekday

sealed interface TimetableUiState {

    @Immutable
    data class Timetable(
        val timetable: Map<Weekday, List<AvailableLesson>>,
        val weekdays: List<Weekday>,
        val todayWeekday: Weekday,
    ) : TimetableUiState

    object Loading : TimetableUiState

    data class Error(val exception: Throwable) : TimetableUiState

    object CourseNotSelected : TimetableUiState

}