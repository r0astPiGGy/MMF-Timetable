package com.rodev.mmf_timetable.domain.model

sealed class WidgetState {
    abstract val lesson: Lesson?

    data class OngoingLesson(
        override val lesson: Lesson,
        val minutesRemaining: Int,
        val progress: Float
    ) : WidgetState()

    data class NextLesson(
        override val lesson: Lesson,
        val minutesBeforeStart: Int
    ) : WidgetState()

    object NoLesson : WidgetState() {
        override val lesson: Lesson? = null
    }
}