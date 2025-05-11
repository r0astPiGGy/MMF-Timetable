package com.rodev.mmf_timetable.feature.teachers.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rodev.mmf_timetable.feature.teachers.TeachersRoute
import kotlinx.serialization.Serializable

@Serializable
object TeachersRoute

fun NavController.navigateToTeachers(navOptions: NavOptions) {
    navigate(TeachersRoute, navOptions = navOptions)
}

fun NavGraphBuilder.teachersScreen(
    onGotoTeacher: (Long) -> Unit
) {
    composable<TeachersRoute> {
        TeachersRoute(onGotoTeacher = onGotoTeacher)
    }
}
