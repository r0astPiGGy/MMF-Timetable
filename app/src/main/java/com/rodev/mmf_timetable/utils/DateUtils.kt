package com.rodev.mmf_timetable.utils

import com.rodev.mmf_timetable.core.model.data.Weekday
import java.util.Calendar

object DateUtils {

    fun getCurrentWeekday(): com.rodev.mmf_timetable.core.model.data.Weekday {
        val calendar = Calendar.getInstance()
        val week = calendar[Calendar.DAY_OF_WEEK]

        return com.rodev.mmf_timetable.core.model.data.Weekday.values()[week - 1]
    }

    fun getWeekOfYear(): Int {
        return Calendar.getInstance()[Calendar.WEEK_OF_YEAR]
    }

}
