package com.rodev.mmf_timetable.feature.preferences.group

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Course

sealed interface GroupUiState {

    @Immutable
    data class Courses(
        val courses: List<Course>,
        val selectedCourse: Int = courses.first().course
    ) : GroupUiState

    object Loading : GroupUiState

    data class Error(val exception: Throwable) : GroupUiState

}