package com.rodev.mmf_timetable.feature.teacher

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.Teacher
import com.rodev.mmf_timetable.core.ui.DateWeekday
import com.rodev.mmf_timetable.core.domain.CurrentLesson

sealed interface TeacherUiState {

    @Immutable
    data class TeacherDetails(
        val teacher: Teacher,
        val currentLesson: CurrentLesson,
        val selectedDate: DateWeekday,
        val week: List<DateWeekday>,
        val timetable: Map<DateWeekday, List<AvailableLesson>>
    ) : TeacherUiState

    object Loading : TeacherUiState

    object NotFound : TeacherUiState

    data class Error(val exception: Throwable) : TeacherUiState

}