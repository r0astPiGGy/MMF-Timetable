package com.rodev.mmf_timetable.core.model.data

data class Lesson(
    val weekday: Weekday,
    val type: Type?,
    val classroom: String,
    val subject: String,
    val timeStartMinutes: Int,
    val timeEndMinutes: Int,
    val teacher: String,
    val remarks: String?,
    val weekType: WeekType?
) {
    enum class Type {
        LECTURE,
        PRACTICE
    }

    enum class WeekType {
        ODD,
        EVEN
    }
}


