package com.rodev.mmf_timetable.data.source.network.model

import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.model.Weekday

data class NetworkLesson(
    val weekday: String,
    val time: String,
    val remarks: String,
    val subjectTeachers: String,
    val lecturePractice: String,
    val room: String
) {
    fun asExternalModel(): Lesson {
        return toLesson(this)
    }
}

private val mappedWeekdays = mapOf(
    "Понедельник" to Weekday.MONDAY,
    "Вторник" to Weekday.TUESDAY,
    "Среда" to Weekday.WEDNESDAY,
    "Четверг" to Weekday.THURSDAY,
    "Пятница" to Weekday.FRIDAY,
    "Суббота" to Weekday.SATURDAY,
    "Воскресенье" to Weekday.SUNDAY,
)

private val mappedLessonTypeEntry = mapOf(
    "п" to Lesson.Type.PRACTICE,
    "л" to Lesson.Type.LECTURE,
    "лаб" to Lesson.Type.PRACTICE
)

private fun parseTotalMinutes(timestamp: String): Int {
    val split = timestamp.split(":")
    val hours = split[0].toIntOrNull() ?: 0
    val minutes = split.getOrNull(1)?.toIntOrNull() ?: 0

    return (hours * 60) + minutes
}

private fun toLesson(networkLesson: NetworkLesson): Lesson {
    val split = networkLesson.subjectTeachers.split("\n")

    val subject = split[0]
    val teacher = split.getOrNull(1) ?: ""

    val weekday = networkLesson.weekday.let(mappedWeekdays::get) ?: throw IllegalStateException()
    val type = mappedLessonTypeEntry[networkLesson.lecturePractice]

    val timeSplit = networkLesson.time.split("–")
    val timeStart = timeSplit[0]
    val timeEnd = timeSplit.getOrNull(1) ?: ""

    return Lesson(
        weekday = weekday,
        type = type,
        classroom = networkLesson.room,
        subject = subject,
        teacher = teacher,
        timeStartMinutes = parseTotalMinutes(timeStart),
        timeEndMinutes = parseTotalMinutes(timeEnd),
        remarks = networkLesson.remarks,
        weekType = when {
            networkLesson.remarks.contains("1н") -> Lesson.WeekType.ODD
            networkLesson.remarks.contains("2н") -> Lesson.WeekType.EVEN
            else -> null
        }
    )
}