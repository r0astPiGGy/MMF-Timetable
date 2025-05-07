package com.rodev.mmf_timetable.feature.preferences.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.rodev.mmf_timetable.feature.preferences.group.GroupRoute
import com.rodev.mmf_timetable.feature.preferences.group.groupScreen
import com.rodev.mmf_timetable.feature.preferences.subgroup.SubgroupRoute
import com.rodev.mmf_timetable.feature.preferences.subgroup.navigateToSubgroup
import kotlinx.serialization.Serializable

@Serializable
object PreferencesRoute

fun NavController.navigateToPreferences(navOptions: NavOptions)
        = navigate(PreferencesRoute, navOptions = navOptions)

fun NavGraphBuilder.preferencesScreen(navController: NavController, onNavigateToHome: () -> Unit) {
    navigation<PreferencesRoute>(startDestination = GroupRoute) {
        groupScreen(
            onNavBack = { navController.navigateUp() },
            onGotoSubgroupScreen = { navController.navigateToSubgroup(groupId = it, navOptions {  }) }
        )
        composable<SubgroupRoute> {
            SubgroupRoute(
                onGotoHome = onNavigateToHome
            )
        }
    }
}
