package com.rodev.mmf_timetable.data.source.network.model

import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.Weekday

data class NetworkLesson(
    val weekday: String,
    val time: String,
    val remarks: String,
    val subjectTeachers: String,
    val lecturePractice: String,
    val room: String
) {
    fun asExternalModel(): com.rodev.mmf_timetable.core.model.data.Lesson {
        return toLesson(this)
    }
}

private val mappedWeekdays = mapOf(
    "Понедельник" to com.rodev.mmf_timetable.core.model.data.Weekday.MONDAY,
    "Вторник" to com.rodev.mmf_timetable.core.model.data.Weekday.TUESDAY,
    "Среда" to com.rodev.mmf_timetable.core.model.data.Weekday.WEDNESDAY,
    "Четверг" to com.rodev.mmf_timetable.core.model.data.Weekday.THURSDAY,
    "Пятница" to com.rodev.mmf_timetable.core.model.data.Weekday.FRIDAY,
    "Суббота" to com.rodev.mmf_timetable.core.model.data.Weekday.SATURDAY,
    "Воскресенье" to com.rodev.mmf_timetable.core.model.data.Weekday.SUNDAY,
)

private val mappedLessonTypeEntry = mapOf(
    "п" to com.rodev.mmf_timetable.core.model.data.Lesson.Type.PRACTICE,
    "л" to com.rodev.mmf_timetable.core.model.data.Lesson.Type.LECTURE,
    "лаб" to com.rodev.mmf_timetable.core.model.data.Lesson.Type.PRACTICE
)

private fun parseTotalMinutes(timestamp: String): Int {
    val split = timestamp.split(":")
    val hours = split[0].toIntOrNull() ?: 0
    val minutes = split.getOrNull(1)?.toIntOrNull() ?: 0

    return (hours * 60) + minutes
}

private fun toLesson(networkLesson: NetworkLesson): com.rodev.mmf_timetable.core.model.data.Lesson {
    val split = networkLesson.subjectTeachers.split("\n")

    val subject = split[0]
    val teacher = split.getOrNull(1) ?: ""

    val weekday = networkLesson.weekday.let(mappedWeekdays::get) ?: throw IllegalStateException()
    val type = mappedLessonTypeEntry[networkLesson.lecturePractice]

    val timeSplit = networkLesson.time.split("–")
    val timeStart = timeSplit[0]
    val timeEnd = timeSplit.getOrNull(1) ?: ""

    return com.rodev.mmf_timetable.core.model.data.Lesson(
        weekday = weekday,
        type = type,
        classroom = networkLesson.room,
        subject = subject,
        teacher = teacher,
        timeStartMinutes = parseTotalMinutes(timeStart),
        timeEndMinutes = parseTotalMinutes(timeEnd),
        remarks = networkLesson.remarks,
        weekType = when {
            networkLesson.remarks.contains("1н") -> com.rodev.mmf_timetable.core.model.data.Lesson.WeekType.ODD
            networkLesson.remarks.contains("2н") -> com.rodev.mmf_timetable.core.model.data.Lesson.WeekType.EVEN
            else -> null
        }
    )
}