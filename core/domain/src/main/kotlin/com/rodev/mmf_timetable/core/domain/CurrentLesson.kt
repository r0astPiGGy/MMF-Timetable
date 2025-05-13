package com.rodev.mmf_timetable.core.domain

import com.rodev.mmf_timetable.core.model.data.Lesson
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

sealed interface CurrentLesson {

    data class Current(
        val lesson: Lesson,
        val remaining: DateTimePeriod,
        val progress: Float
    ) : CurrentLesson

    data class Next(
        val lesson: Lesson,
        val remaining: DateTimePeriod
    ) : CurrentLesson

    object NoLessonToday : CurrentLesson

}

fun getTeacherCurrentLessonIn(lessons: List<Lesson>): CurrentLesson {
    return getCurrentLessonIn(lessons) { date ->
        isAvailable(date) && isWeekdayMatches(date)
    }
}

fun getCurrentLessonIn(lessons: List<Lesson>, subGroups: Set<Long>): CurrentLesson {
    return getCurrentLessonIn(lessons) { date ->
        isAvailable(date, subGroups) && isWeekdayMatches(date)
    }
}

inline fun getCurrentLessonIn(lessons: List<Lesson>, isAvailable: Lesson.(LocalDate) -> Boolean): CurrentLesson {
    val tz = TimeZone.currentSystemDefault()
    val now = Clock.System.now()
    val dateTime = now.toLocalDateTime(tz)
//    val dateTime = LocalDateTime(
//        year = 2025,
//        dayOfMonth = 8,
//        month = Month.MAY,
//        hour = 10,
//        minute = 40,
//    )
//    val now = dateTime.toInstant(tz)
    val availableLessons = lessons.filter { it.isAvailable(dateTime.date) }
    val currentLesson = availableLessons.firstOrNull { it.isInTime(dateTime.time) }

    if (currentLesson == null) {
        // find next lesson today
        val nextLesson = availableLessons.firstOrNull { it.isSoon(dateTime.time) }
        if (nextLesson == null) {
            return CurrentLesson.NoLessonToday
        }

        val end = LocalDateTime(
            date = dateTime.date,
            time = nextLesson.timeStart
        ).toInstant(tz)

        val remaining = now.periodUntil(end, tz)

        return CurrentLesson.Next(lesson = nextLesson, remaining = remaining)
    }

    val timeStartMillis = currentLesson.timeStart.toMillisecondOfDay()
    val timeEnd = currentLesson.timeEnd
    val nowMillis = dateTime.time.toMillisecondOfDay()

    val progress = (nowMillis - timeStartMillis).toFloat()
        .div(timeEnd.toMillisecondOfDay() - timeStartMillis)

    val end = LocalDateTime(
        date = dateTime.date,
        time = timeEnd
    ).toInstant(tz)

    val remaining = now.periodUntil(end, tz)

    return CurrentLesson.Current(
        lesson = currentLesson,
        remaining = remaining,
        progress = progress
    )
}
