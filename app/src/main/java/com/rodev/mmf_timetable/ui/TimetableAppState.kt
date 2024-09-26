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
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import com.rodev.mmf_timetable.core.model.data.UserData
import com.rodev.mmf_timetable.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberTimetableAppState(
    userDataRepository: UserDataRepository,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): TimetableAppState {
    return remember(userDataRepository, navController, coroutineScope) {
        TimetableAppState(coroutineScope, navController, userDataRepository)
    }
}

@Stable
class TimetableAppState(
    coroutineScope: CoroutineScope,
    val navController: NavHostController,
    userDataRepository: UserDataRepository
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

}