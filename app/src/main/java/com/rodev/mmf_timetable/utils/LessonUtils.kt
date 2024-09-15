package com.rodev.mmf_timetable.utils

import com.rodev.mmf_timetable.core.model.data.Lesson

fun Int.toDisplayableTime(): String {
    val hours = this / 60
    val minutes = this % 60

    // TODO String.format %00 ??
    fun timeToString(time: Int): String {
        return if (time < 10) {
            "0$time"
        } else {
            "$time"
        }
    }

    return timeToString(hours) + ":" + timeToString(minutes)
}

val Lesson.displayableTimeStart
    get() = timeStartMinutes.toDisplayableTime()

val Lesson.displayableTimeEnd
    get() = timeEndMinutes.toDisplayableTime()