package com.rodev.mmf_timetable.core.model.data

import kotlinx.datetime.LocalDate

sealed interface Availability {

    data class After(val date: LocalDate): Availability

    data class Until(val date: LocalDate): Availability

    data class InRange(val from: LocalDate, val to: LocalDate): Availability

    data class Except(val dates: List<LocalDate>): Availability

    data class Only(val dates: List<LocalDate>): Availability

    object OddWeek : Availability
    object EvenWeek : Availability
}