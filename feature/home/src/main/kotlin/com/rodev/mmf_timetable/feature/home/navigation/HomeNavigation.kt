package com.rodev.mmf_timetable.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rodev.mmf_timetable.feature.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(HomeRoute, navOptions = navOptions)
}

fun NavGraphBuilder.homeScreen(
    onGotoTeacher: (Long) -> Unit,
    onGotoRoom: (Long) -> Unit,
    onGotoTeachers: () -> Unit,
    onGotoRooms: () -> Unit,
    onChangeGroup: () -> Unit,
    onAddWidget: () -> Unit,
) {
    composable<HomeRoute> {
        HomeRoute(
            onGotoRooms = onGotoRooms,
            onChangeGroup = onChangeGroup,
            onGotoRoom = onGotoRoom,
            onGotoTeacher = onGotoTeacher,
            onGotoTeachers = onGotoTeachers,
            onAddWidget = onAddWidget
        )
    }
}
