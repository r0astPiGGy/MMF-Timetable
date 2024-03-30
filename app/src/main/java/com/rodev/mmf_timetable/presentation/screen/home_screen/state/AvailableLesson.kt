package com.rodev.mmf_timetable.presentation.screen.home_screen.state

import com.rodev.mmf_timetable.domain.model.Lesson
import java.util.UUID

class AvailableLesson(
    val id: String = UUID.randomUUID().toString(),
    val wrappedLesson: Lesson,
    val available: Boolean
)
