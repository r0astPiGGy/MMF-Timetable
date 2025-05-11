package com.rodev.mmf_timetable.feature.classrooms.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rodev.mmf_timetable.feature.classrooms.ClassroomsRoute
import kotlinx.serialization.Serializable

@Serializable
object ClassroomsRoute

fun NavController.navigateToClassrooms(navOptions: NavOptions) {
    navigate(ClassroomsRoute, navOptions = navOptions)
}

fun NavGraphBuilder.classroomsScreen(
    onGotoClassroom: (Long) -> Unit
) {
    composable<ClassroomsRoute> {
        ClassroomsRoute(onGotoClassroom = onGotoClassroom)
    }
}
