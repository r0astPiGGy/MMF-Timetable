package com.rodev.mmf_timetable.feature.timetable.model

import androidx.compose.runtime.Immutable

@Immutable
data class Week(
    val days: List<DateWeekday>,
) {

    override fun hashCode(): Int {
        return days.fold(1) { acc, w -> 31 * acc + w.hashCode() }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Week

        return days.containsAll(other.days)
    }

}
