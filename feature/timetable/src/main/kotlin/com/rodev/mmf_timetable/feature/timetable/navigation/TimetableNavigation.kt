package com.rodev.mmf_timetable.feature.timetable.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rodev.mmf_timetable.feature.timetable.TimetableRoute
import kotlinx.serialization.Serializable

@Serializable
object TimetableRoute

fun NavController.navigateToTimetable(navOptions: NavOptions)
    = navigate(TimetableRoute, navOptions = navOptions)

fun NavGraphBuilder.timetableScreen(
    onGotoRoom: (Long) -> Unit,
    onGotoTeacher: (Long) -> Unit,
) {
    composable<TimetableRoute> {
        TimetableRoute(
            onGotoTeacher = onGotoTeacher,
            onGotoRoom = onGotoRoom
        )
    }
}