package com.rodev.mmf_timetable.feature.preferences.group

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
internal object GroupRoute

internal fun NavController.navigateToGroup(navOptions: NavOptions)
        = navigate(GroupRoute, navOptions = navOptions)

internal fun NavGraphBuilder.groupScreen(
    onNavBack: () -> Unit,
    onGotoSubgroupScreen: (String) -> Unit,
) {
    composable<GroupRoute> {
        GroupScreenRoute(
            onNavBack = onNavBack,
            onGotoSubgroupScreen = onGotoSubgroupScreen
        )
    }
}
