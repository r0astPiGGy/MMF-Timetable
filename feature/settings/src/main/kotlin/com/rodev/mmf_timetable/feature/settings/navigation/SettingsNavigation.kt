package com.rodev.mmf_timetable.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rodev.mmf_timetable.feature.settings.SettingsRoute
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

fun NavController.navigateToSettings(navOptions: NavOptions) {
    navigate(SettingsRoute, navOptions = navOptions)
}

fun NavGraphBuilder.settingsScreen() {
    composable<SettingsRoute> {
        SettingsRoute()
    }
}
