package com.rodev.mmf_timetable.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.rodev.mmf_timetable.feature.classroom.navigation.classroomScreen
import com.rodev.mmf_timetable.feature.classroom.navigation.navigateToClassroom
import com.rodev.mmf_timetable.feature.classrooms.navigation.classroomsScreen
import com.rodev.mmf_timetable.feature.classrooms.navigation.navigateToClassrooms
import com.rodev.mmf_timetable.feature.home.navigation.HomeRoute
import com.rodev.mmf_timetable.feature.home.navigation.homeScreen
import com.rodev.mmf_timetable.feature.preferences.navigation.navigateToPreferences
import com.rodev.mmf_timetable.feature.preferences.navigation.preferencesScreen
import com.rodev.mmf_timetable.feature.settings.navigation.settingsScreen
import com.rodev.mmf_timetable.feature.teacher.navigation.navigateToTeacher
import com.rodev.mmf_timetable.feature.teacher.navigation.teacherScreen
import com.rodev.mmf_timetable.feature.teachers.navigation.navigateToTeachers
import com.rodev.mmf_timetable.feature.teachers.navigation.teachersScreen
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
            onGotoRoom = appState::navigateToClassroom,
            onGotoTeacher = appState::navigateToTeacher
        )
        homeScreen(
            onAddWidget = appState::requestAddWidget,
            onChangeGroup = { navController.navigateToPreferences(navOptions { launchSingleTop = true }) },
            onGotoTeachers = appState::navigateToTeachers,
            onGotoRooms = appState::navigateToClassrooms,
            onGotoRoom = appState::navigateToClassroom,
            onGotoTeacher = appState::navigateToTeacher
        )
        teacherScreen(
            onGotoRoom = appState::navigateToClassroom,
            onGotoTeacher = appState::navigateToTeacher
        )
        classroomScreen()
        teachersScreen(
            onGotoTeacher = appState::navigateToTeacher
        )
        classroomsScreen(
            onGotoClassroom = appState::navigateToClassroom,
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