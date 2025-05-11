package com.rodev.mmf_timetable.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkTeacher(
    val id: Long,
    val name: String,
    @SerialName("fullname")
    val fullName: String? = null,
    @SerialName("imageurl")
    val imageUrl: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val position: String? = null
)
