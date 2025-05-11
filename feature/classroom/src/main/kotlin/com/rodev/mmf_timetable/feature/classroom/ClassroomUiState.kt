package com.rodev.mmf_timetable.feature.classroom

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Classroom

sealed interface ClassroomUiState {

    @Immutable
    data class ClassroomDetails(val classroom: Classroom) : ClassroomUiState

    object Loading : ClassroomUiState

    object NotFound : ClassroomUiState

    data class Error(val exception: Throwable) : ClassroomUiState

}