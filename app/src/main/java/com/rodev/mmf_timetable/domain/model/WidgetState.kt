package com.rodev.mmf_timetable.domain.model

sealed class WidgetState {
    abstract val lesson: com.rodev.mmf_timetable.core.model.data.Lesson?

    data class OngoingLesson(
        override val lesson: com.rodev.mmf_timetable.core.model.data.Lesson,
        val minutesRemaining: Int,
        val progress: Float
    ) : WidgetState()

    data class NextLesson(
        override val lesson: com.rodev.mmf_timetable.core.model.data.Lesson,
        val minutesBeforeStart: Int
    ) : WidgetState()

    object NoLesson : WidgetState() {
        override val lesson: com.rodev.mmf_timetable.core.model.data.Lesson? = null
    }
}