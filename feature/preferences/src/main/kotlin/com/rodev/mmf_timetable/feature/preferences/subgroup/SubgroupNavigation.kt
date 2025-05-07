package com.rodev.mmf_timetable.feature.preferences.subgroup

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
internal data class SubgroupRoute(
    val groupId: String
)

internal fun NavController.navigateToSubgroup(groupId: String, navOptions: NavOptions)
        = navigate(SubgroupRoute(groupId), navOptions = navOptions)
