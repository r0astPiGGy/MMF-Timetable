package com.rodev.mmf_timetable.core.network.model

data class NetworkSubgroupSubject(
    val id: Long,
    val name: String,
    val subgroups: List<NetworkSubgroup>
)
