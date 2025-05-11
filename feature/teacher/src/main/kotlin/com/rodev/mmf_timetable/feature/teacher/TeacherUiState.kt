package com.rodev.mmf_timetable.feature.teacher

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Teacher
import com.rodev.mmf_timetable.feature.teacher.model.CurrentLesson

sealed interface TeacherUiState {

    @Immutable
    data class TeacherDetails(
        val teacher: Teacher,
        val currentLesson: CurrentLesson
    ) : TeacherUiState

    object Loading : TeacherUiState

    object NotFound : TeacherUiState

    data class Error(val exception: Throwable) : TeacherUiState

}