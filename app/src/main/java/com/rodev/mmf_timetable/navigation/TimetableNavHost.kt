package com.rodev.mmf_timetable.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.rodev.mmf_timetable.feature.home.navigation.HomeRoute
import com.rodev.mmf_timetable.feature.home.navigation.homeScreen
import com.rodev.mmf_timetable.feature.preferences.navigation.navigateToPreferences
import com.rodev.mmf_timetable.feature.preferences.navigation.preferencesScreen
import com.rodev.mmf_timetable.feature.settings.navigation.settingsScreen
import com.rodev.mmf_timetable.feature.timetable.navigation.TimetableRoute
import com.rodev.mmf_timetable.feature.timetable.navigation.navigateToTimetable
import com.rodev.mmf_timetable.feature.timetable.navigation.timetableScreen
import com.rodev.mmf_timetable.ui.TimetableAppState

@Composable
fun TimetableNavHost(
    modifier: Modifier = Modifier,
    appState: TimetableAppState,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        timetableScreen(
            onGotoRoom = {

            },
            onGotoTeacher = {

            }
        )
        homeScreen(
            onAddWidget = appState::requestAddWidget,
            onChangeGroup = { navController.navigateToPreferences(navOptions { launchSingleTop = true }) },
            onGotoTeachers = { },
            onGotoRooms = { }
        )
        settingsScreen()
        preferencesScreen(
            navController = navController,
            onNavigateToHome = {
                navController.popBackStack(HomeRoute, false)
            }
        )
    }
}