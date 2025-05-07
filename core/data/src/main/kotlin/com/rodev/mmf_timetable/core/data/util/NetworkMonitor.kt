package com.rodev.mmf_timetable.core.data.util

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {

    val isOnline: Flow<Boolean>

}