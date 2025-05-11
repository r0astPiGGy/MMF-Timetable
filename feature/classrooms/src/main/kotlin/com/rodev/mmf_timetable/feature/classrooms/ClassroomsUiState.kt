package com.rodev.mmf_timetable.feature.classrooms

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Classroom

sealed interface ClassroomsUiState {

    @Immutable
    data class Classrooms(val classrooms: List<Classroom>) : ClassroomsUiState

    object Loading : ClassroomsUiState

    data class Error(val exception: Throwable) : ClassroomsUiState

}