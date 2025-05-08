package com.rodev.mmf_timetable.feature.home

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.Group
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.Weekday

sealed interface HomeUiState {

    @Immutable
    data class Home(
        val group: Group?,
        val currentLesson: Lesson?
    ) : HomeUiState

    object Loading : HomeUiState

    data class Error(val exception: Throwable) : HomeUiState

    object CourseNotSelected : HomeUiState

}