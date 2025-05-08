package com.rodev.mmf_timetable.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.rodev.mmf_timetable.R
import com.rodev.mmf_timetable.feature.home.navigation.HomeRoute
import com.rodev.mmf_timetable.feature.settings.navigation.SettingsRoute
import com.rodev.mmf_timetable.feature.timetable.navigation.TimetableRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val titleTextId: Int,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    val route: KClass<*>
) {
    HOME(
        titleTextId = R.string.home,
        route = HomeRoute::class,
        icon = Icons.Outlined.Home,
        iconSelected = Icons.Filled.Home
    ),
    TIMETABLE(
        titleTextId = R.string.timetable,
        route = TimetableRoute::class,
        icon = Icons.Outlined.CalendarToday,
        iconSelected = Icons.Filled.CalendarToday
    ),
    SETTINGS(
        titleTextId = R.string.settings,
        route = SettingsRoute::class,
        icon = Icons.Outlined.Settings,
        iconSelected = Icons.Filled.Settings
    ),
}