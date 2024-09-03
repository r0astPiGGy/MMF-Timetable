package com.rodev.mmf_timetable.domain.model

data class TimetableData(
    val week: Int,
    val course: Int,
    val lessons: Map<Weekday, List<Lesson>>,
    val group: String,
    val dirty: Boolean = false
)