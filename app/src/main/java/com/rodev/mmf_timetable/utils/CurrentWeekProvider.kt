package com.rodev.mmf_timetable.utils

import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date

object CurrentWeekProvider {

    fun weekOf(period: LongRange, date: Long): Long? {
        return weekOf(
            start = Date(period.first),
            end = Date(period.last),
            date = Date(date)
        )
    }

    fun weekOf(start: Date, end: Date, date: Date): Long? {
        if (date !in start..end) return null
        val zone = ZoneId.systemDefault()

        return ChronoUnit.WEEKS.between(
            start.toInstant().atZone(zone),
            date.toInstant().atZone(zone)
        ) + 1
    }

    fun dateOf(day: Int, month: Int): Date {
        val calendar = Calendar.getInstance()

        val year = calendar[Calendar.YEAR]

        calendar.clear()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = day

        return calendar.time
    }

    fun millisOf(day: Int, month: Int): Long {
        return dateOf(day, month).time
    }

}