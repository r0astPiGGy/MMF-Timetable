package com.rodev.mmf_timetable.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkGroup(
    val id: String,
    val name: String
)
