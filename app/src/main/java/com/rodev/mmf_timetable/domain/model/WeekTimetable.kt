package com.rodev.mmf_timetable.domain.model

data class WeekTimetable(
    val map: Map<Weekday, List<Lesson>>,
)

operator fun WeekTimetable.get(weekday: Weekday): List<Lesson> {
    return map[weekday]!!
}