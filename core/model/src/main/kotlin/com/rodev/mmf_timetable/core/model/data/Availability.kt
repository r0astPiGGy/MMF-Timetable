package com.rodev.mmf_timetable.core.model.data

import kotlinx.datetime.LocalDate

sealed interface Availability {

    data class After(val date: LocalDate, val weekType: WeekType?): Availability

    data class Until(val date: LocalDate, val weekType: WeekType?): Availability

    data class InRange(val from: LocalDate, val to: LocalDate, val weekType: WeekType?): Availability

    data class Except(val dates: List<LocalDate>, val weekType: WeekType?): Availability

    data class Only(val dates: List<LocalDate>, val weekType: WeekType?): Availability

    data class ByWeekType(val weekType: WeekType): Availability
}