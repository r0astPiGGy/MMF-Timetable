package com.rodev.mmf_timetable.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkLessonTeacher(
    val id: Long,
    val name: String,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    val position: String? = null,
)

@Serializable
data class NetworkLesson(
    val id: Long,
    val group: NetworkGroup?,
    val classroom: NetworkClassroom?,
    val subgroup: NetworkSubgroup?,
    val teachers: List<NetworkLessonTeacher>,
    val subject: String?,
    @SerialName("additional_info")
    val additionalInfo: List<String>,
    val availability: List<NetworkAvailability>,
    @SerialName("timestart")
    val timeStart: String,
    @SerialName("timeend")
    val timeEnd: String,
    val type: String?,
    val weekday: String
)

