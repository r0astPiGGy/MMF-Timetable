package com.rodev.mmf_timetable.core.model.data

data class SubgroupSubject(
    val id: Long,
    val name: String,
    val subgroups: List<Subgroup>
)
