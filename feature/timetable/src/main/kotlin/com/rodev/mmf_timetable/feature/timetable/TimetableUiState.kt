package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.ui.DateWeekday

sealed interface TimetableUiState {

    @Immutable
    data class Timetable(
        val timetable: Map<DateWeekday, List<AvailableLesson>>,
        val week: List<DateWeekday>,
        val selectedDate: DateWeekday,
    ) : TimetableUiState

    object Loading : TimetableUiState

    data class Error(val exception: Throwable) : TimetableUiState

    object CourseNotSelected : TimetableUiState

}