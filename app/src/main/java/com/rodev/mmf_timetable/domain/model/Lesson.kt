package com.rodev.mmf_timetable.domain.model

import androidx.room.Entity

@Entity
data class Lesson(
    val weekday: Weekday,
    val type: Type?,
    val classroom: String,
    val subject: String,
    val timeStartMinutes: Int,
    val timeEndMinutes: Int,
    val teacher: String,
    val remarks: String?,
    val week: WeekType?
) {

    enum class Type {
        LECTURE,
        PRACTICE
    }

    enum class WeekType {
        ODD,
        EVEN
    }

    companion object {

        val subGroups = listOf("а", "б")

    }

}


