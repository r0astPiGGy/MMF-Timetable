package com.rodev.mmf_timetable

import com.rodev.mmf_timetable.core.model.data.Weekday
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Assert.*
import org.junit.Test

class CurrentWeekProviderTests {

    fun Weekday.isWeekdayMatches(date: LocalDate): Boolean {
        val index = date.dayOfWeek.value - 1
        val dayOfWeek = listOf(
            Weekday.MONDAY,
            Weekday.TUESDAY,
            Weekday.WEDNESDAY,
            Weekday.THURSDAY,
            Weekday.FRIDAY,
            Weekday.SATURDAY,
            Weekday.SUNDAY
        )
        return dayOfWeek[index] == this
    }

    @Test
    fun `should determine valid weekday`() {
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        assertTrue(Weekday.SUNDAY.isWeekdayMatches(time.date))
        assertFalse(Weekday.WEDNESDAY.isWeekdayMatches(time.date))
    }

}