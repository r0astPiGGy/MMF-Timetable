package com.rodev.mmf_timetable.core.network.model

data class NetworkCourse(
    val course: Int,
    val groups: List<NetworkGroup>
)
