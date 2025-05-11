package com.rodev.mmf_timetable.feature.teachers

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Teacher

sealed interface TeachersUiState {

    @Immutable
    data class Teachers(val teachers: List<Teacher>) : TeachersUiState

    object Loading : TeachersUiState

    object Empty: TeachersUiState

    object NothingFoundByQuery : TeachersUiState

    data class Error(val exception: Throwable) : TeachersUiState

}