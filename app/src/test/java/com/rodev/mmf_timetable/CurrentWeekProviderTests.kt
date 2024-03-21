package com.rodev.mmf_timetable

import com.rodev.mmf_timetable.utils.CurrentWeekProvider
import org.junit.Assert.*
import org.junit.Test

class CurrentWeekProviderTests {

    @Test
    fun current_week_is_7() {
        val start = CurrentWeekProvider.dateOf(5, 2)
        val end = CurrentWeekProvider.dateOf(8, 6)
        val today = CurrentWeekProvider.dateOf(21, 3)

        assertEquals(
            CurrentWeekProvider.weekOf(start, end, today),
            7L
        )
    }

    @Test
    fun week_is_null_before() {
        val start = CurrentWeekProvider.dateOf(5, 2)
        val end = CurrentWeekProvider.dateOf(8, 6)
        val today = CurrentWeekProvider.dateOf(21, 1)

        assertNull(
            CurrentWeekProvider.weekOf(start, end, today)
        )
    }

    @Test
    fun week_is_null_after() {
        val start = CurrentWeekProvider.dateOf(5, 1)
        val end = CurrentWeekProvider.dateOf(8, 5)
        val today = CurrentWeekProvider.dateOf(9, 5)

        assertNull(
            CurrentWeekProvider.weekOf(start, end, today)
        )
    }

}