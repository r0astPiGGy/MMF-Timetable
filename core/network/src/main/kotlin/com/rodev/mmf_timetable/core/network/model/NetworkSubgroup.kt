package com.rodev.mmf_timetable.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkSubgroup(
    val id: Long,
    val name: String
)
