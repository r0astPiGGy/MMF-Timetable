package com.rodev.mmf_timetable.core.model.data

data class Lesson(
    val id: Long,
    val subGroup: Subgroup?,
    val classroom: Classroom?,
    val subject: String,
    val teachers: List<LessonTeacher>,
    val additionalInfo: List<String>,
    val timeStartMinutes: Int,
    val timeEndMinutes: Int,
    val timeStart: String,
    val timeEnd: String,
    val availability: List<Availability>,
    val type: String?,
    val weekday: Weekday
)


