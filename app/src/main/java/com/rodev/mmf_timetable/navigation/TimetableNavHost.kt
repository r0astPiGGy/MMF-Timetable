package com.rodev.mmf_timetable.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.rodev.mmf_timetable.feature.timetable.navigation.TIMETABLE_ROUTE
import com.rodev.mmf_timetable.feature.timetable.navigation.timetableScreen
import com.rodev.mmf_timetable.ui.TimetableAppState

@Composable
fun TimetableNavHost(
    modifier: Modifier = Modifier,
    appState: TimetableAppState,
    startDestination: String = TIMETABLE_ROUTE
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        timetableScreen()
    }
}