package com.rodev.mmf_timetable.presentation.screen.home_screen.state

import com.rodev.mmf_timetable.domain.model.Lesson

class AvailableLesson(
    val wrappedLesson: Lesson,
    val available: Boolean
)
