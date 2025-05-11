package com.rodev.mmf_timetable.core.model.data

import kotlinx.datetime.LocalTime

data class Lesson(
    val id: Long,
    val subGroup: Subgroup?,
    val classroom: Classroom?,
    val group: Group?,
    val subject: String,
    val teachers: List<LessonTeacher>,
    val additionalInfo: List<String>,
    val timeStart: LocalTime,
    val timeEnd: LocalTime,
    val availability: List<Availability>,
    val type: String?,
    val weekday: Weekday
)


