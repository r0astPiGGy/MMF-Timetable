package com.rodev.mmf_timetable.feature.timetable.model

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Weekday
import kotlinx.datetime.LocalDate

@Immutable
data class DateWeekday(
    val date: LocalDate,
    val weekday: Weekday
)
