package com.rodev.mmf_timetable.utils

import com.rodev.mmf_timetable.domain.model.Lesson

fun Int.toDisplayableTime(): String {
    val hours = this / 60
    val minutes = this % 60

    fun timeToString(time: Int): String {
        return if (time < 10) {
            "0$time"
        } else {
            "$time"
        }
    }

    return timeToString(hours) + ":" + timeToString(minutes)
}

private fun isLessonAvailable(lesson: Lesson): Boolean {
    val weekOfYear = DateUtils.getWeekOfYear()

    return when (lesson.week) {
        Lesson.WeekType.ODD -> weekOfYear % 2 != 0
        Lesson.WeekType.EVEN -> weekOfYear % 2 == 0
        null -> true
    }
}

val Lesson.available: Boolean
    get() = isLessonAvailable(this)

val Lesson.displayableTimeStart
    get() = timeStartMinutes.toDisplayableTime()

val Lesson.displayableTimeEnd
    get() = timeEndMinutes.toDisplayableTime()