package com.rodev.mmf_timetable.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.rodev.mmf_timetable.feature.timetable.navigation.TimetableRoute
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
        startDestination = TimetableRoute,
        modifier = modifier
    ) {
        timetableScreen()
    }
}