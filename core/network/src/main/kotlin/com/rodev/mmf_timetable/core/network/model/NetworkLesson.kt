package com.rodev.mmf_timetable.core.network.model

data class NetworkLesson(
    val id: Long,
    val groupName: String,
    val classroom: Long?,
    val classroomName: String?,
    val teacher: Long?,
    val teacherName: String?,
    val subgroup: Long?,
    val subgroupName: String?,
    val subject: String?,
    val availability: NetworkAvailability?,
    val timeStart: Int,
    val timeEnd: Int,
    val type: String?,
    val weekday: String
)

