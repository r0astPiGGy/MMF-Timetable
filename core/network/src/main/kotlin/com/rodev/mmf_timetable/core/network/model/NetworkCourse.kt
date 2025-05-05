package com.rodev.mmf_timetable.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCourse(
    val course: Int,
    val groups: List<NetworkGroup>
)
