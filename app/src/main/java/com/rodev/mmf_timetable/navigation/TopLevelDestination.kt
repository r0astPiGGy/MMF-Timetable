package com.rodev.mmf_timetable.navigation

import com.rodev.mmf_timetable.R
import com.rodev.mmf_timetable.feature.timetable.navigation.TimetableRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val titleTextId: Int,
    val route: KClass<*>
) {
    TIMETABLE(R.string.timetable, TimetableRoute::class)
}