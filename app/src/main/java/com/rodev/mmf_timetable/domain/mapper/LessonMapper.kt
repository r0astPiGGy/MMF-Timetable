package com.rodev.mmf_timetable.domain.mapper

import com.rodev.mmf_timetable.domain.model.LessonDto
import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.model.Weekday

object LessonMapper {

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
        "л" to Lesson.Type.LECTURE
    )

    fun <T> mapLessonsByWeekday(data: List<Lesson>, transformFunction: (Lesson) -> T): Map<Weekday, List<T>> {
        val mappedLesson = hashMapOf<Weekday, MutableList<T>>()

        for (lesson in data) {
            mappedLesson.computeIfAbsent(lesson.weekday) { mutableListOf() }.add(transformFunction(lesson))
        }

        return mappedLesson
    }

    private fun parseTotalMinutes(timestamp: String): Int {
        val split = timestamp.split(":")
        val hours = split[0].toIntOrNull() ?: 0
        val minutes = split.getOrNull(1)?.toIntOrNull() ?: 0

        return (hours * 60) + minutes
    }

    fun toLesson(lessonDto: LessonDto): Lesson {
        val split = lessonDto.subjectTeachers.split("\n")

        val subject = split[0]
        val teacher = split.getOrNull(1) ?: ""

        val weekday = lessonDto.weekday.let(mappedWeekdays::get) ?: throw IllegalStateException()
        val type = mappedLessonTypeEntry[lessonDto.lecturePractice]

        val timeSplit = lessonDto.time.split("–")
        val timeStart = timeSplit[0]
        val timeEnd = timeSplit.getOrNull(1) ?: ""

        return Lesson(
            weekday = weekday,
            type = type,
            classroom = lessonDto.room,
            subject = subject,
            teacher = teacher,
            timeStartMinutes = parseTotalMinutes(timeStart),
            timeEndMinutes = parseTotalMinutes(timeEnd),
            remarks = lessonDto.remarks,
            week = when {
                lessonDto.remarks.contains("1н") -> Lesson.WeekType.ODD
                lessonDto.remarks.contains("2н") -> Lesson.WeekType.EVEN
                else -> null
            }
        )
    }

}