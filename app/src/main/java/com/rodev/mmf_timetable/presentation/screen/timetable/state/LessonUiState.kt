package com.rodev.mmf_timetable.presentation.screen.timetable.state

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.domain.model.Lesson
import java.util.UUID

@Immutable
data class LessonUiState(
    val id: String = UUID.randomUUID().toString(),
    val wrappedLesson: Lesson,
    val available: Boolean
)
