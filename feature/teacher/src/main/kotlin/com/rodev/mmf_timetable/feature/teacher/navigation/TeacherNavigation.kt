package com.rodev.mmf_timetable.feature.teacher.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rodev.mmf_timetable.feature.teacher.TeacherRoute
import kotlinx.serialization.Serializable

@Serializable
data class TeacherRoute(val teacherId: Long)

fun NavController.navigateToTeacher(teacherId: Long, navOptions: NavOptions) {
    navigate(TeacherRoute(teacherId), navOptions = navOptions)
}

fun NavGraphBuilder.teacherScreen(
    onGotoRoom: (Long) -> Unit,
    onGotoTeacher: (Long) -> Unit,
) {
    composable<TeacherRoute> {
        TeacherRoute(
            onGotoTeacher = onGotoTeacher,
            onGotoRoom = onGotoRoom
        )
    }
}
