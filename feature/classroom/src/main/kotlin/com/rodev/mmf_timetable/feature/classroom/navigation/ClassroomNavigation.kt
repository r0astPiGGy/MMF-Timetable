package com.rodev.mmf_timetable.feature.classroom.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rodev.mmf_timetable.feature.classroom.ClassroomRoute
import kotlinx.serialization.Serializable

@Serializable
data class ClassroomRoute(val classroomId: Long)

fun NavController.navigateToClassroom(classroomId: Long, navOptions: NavOptions) {
    navigate(ClassroomRoute(classroomId), navOptions = navOptions)
}

fun NavGraphBuilder.classroomScreen() {
    composable<ClassroomRoute> {
        ClassroomRoute()
    }
}
