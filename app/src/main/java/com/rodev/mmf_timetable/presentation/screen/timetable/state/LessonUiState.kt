package com.rodev.mmf_timetable.presentation.screen.timetable.state

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Lesson
import java.util.UUID

@Immutable
data class LessonUiState(
    val id: String = UUID.randomUUID().toString(),
    val wrappedLesson: com.rodev.mmf_timetable.core.model.data.Lesson,
    val available: Boolean
)
