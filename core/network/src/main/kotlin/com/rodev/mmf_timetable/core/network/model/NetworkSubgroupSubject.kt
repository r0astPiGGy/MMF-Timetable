package com.rodev.mmf_timetable.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkSubgroupSubject(
    val id: Long,
    val name: String,
    val subgroups: List<NetworkSubgroup>
)
