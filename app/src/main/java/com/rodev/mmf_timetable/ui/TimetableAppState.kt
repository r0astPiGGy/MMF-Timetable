package com.rodev.mmf_timetable.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.rodev.mmf_timetable.core.data.repository.CourseRepository
import com.rodev.mmf_timetable.core.data.repository.OfflineFirstCourseRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.model.data.UserData
import com.rodev.mmf_timetable.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Composable
fun rememberTimetableAppState(
    userDataRepository: UserDataRepository,
    courseRepository: CourseRepository,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): TimetableAppState {
    return remember(userDataRepository, courseRepository, navController, coroutineScope) {
        TimetableAppState(coroutineScope, navController, userDataRepository, courseRepository)
    }
}

@Stable
class TimetableAppState(
    private val coroutineScope: CoroutineScope,
    val navController: NavHostController,
    private val userDataRepository: UserDataRepository,
    courseRepository: CourseRepository
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val userData: StateFlow<UserData?> =
        userDataRepository.userData.stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val courses: StateFlow<List<Course>> =
        courseRepository.getCourses().stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val options = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.TIMETABLE -> TODO()
        }
    }

    fun onGroupSelected(course: Int, groupId: String, subgroupId: String?) {
        coroutineScope.launch {
            userDataRepository.updateUserData(UserData(course = course, groupId = groupId, subGroup = subgroupId))
        }
    }
}