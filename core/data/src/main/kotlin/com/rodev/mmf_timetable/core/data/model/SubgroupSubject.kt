package com.rodev.mmf_timetable.core.data.model

import com.rodev.mmf_timetable.core.model.data.Subgroup
import com.rodev.mmf_timetable.core.model.data.SubgroupSubject
import com.rodev.mmf_timetable.core.network.model.NetworkSubgroupSubject

fun NetworkSubgroupSubject.asExternalModel() = SubgroupSubject(
    id = id,
    name = name,
    subgroups = this.subgroups
        .map { Subgroup(it.id, it.name) }
        .sortedBy { it.name }
)