package com.rodev.mmf_timetable.core.model.data

data class StudyPlan(
    val spans: List<TimeSpan>
)

data class TimeSpan(
    val type: String,
    val start: Long,
    val end: Long
)