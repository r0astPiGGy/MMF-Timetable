package com.rodev.mmf_timetable.domain.model

import com.rodev.mmf_timetable.R

enum class Weekday(
    val resId: Int
) {
    SUNDAY(R.string.weekday_sunday),
    MONDAY(R.string.weekday_monday),
    TUESDAY(R.string.weekday_tuesday),
    WEDNESDAY(R.string.weekday_wednesday),
    THURSDAY(R.string.weekday_thursday),
    FRIDAY(R.string.weekday_friday),
    SATURDAY(R.string.weekday_saturday),
}