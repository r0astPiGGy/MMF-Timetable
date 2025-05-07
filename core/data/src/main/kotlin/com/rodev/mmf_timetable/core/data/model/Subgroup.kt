package com.rodev.mmf_timetable.core.data.model

import android.util.Log.v
import com.rodev.mmf_timetable.core.model.data.Subgroup
import com.rodev.mmf_timetable.core.network.model.NetworkSubgroup

fun NetworkSubgroup.asExternalModel(): Subgroup {
    return Subgroup(
        id = id,
        name = name
    )
}