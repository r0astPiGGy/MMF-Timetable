package com.rodev.mmf_timetable.core.model.data

data class Lesson(
    val id: Long,
    val subGroup: String?,
    val subGroupId: Long?,
    val classroom: String?,
    val classroomId: Long?,
    val subject: String,
    val teacher: String?,
    val teacherId: Long?,
    val timeStartMinutes: Int,
    val timeEndMinutes: Int,
    val availability: Availability?,
    val type: String?,
    val weekday: Weekday
)


