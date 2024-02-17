package com.rodev.mmf_timetable

import java.util.concurrent.TimeUnit

object K {

    object Constants {

        // Cache exist 24 hours
        val TIMETABLE_CACHE_INVALIDATION = TimeUnit.HOURS.toMillis(24)

        const val GITHUB_LINK = "https://github.com/r0astPiGGy/MMF-Timetable"
    }

}