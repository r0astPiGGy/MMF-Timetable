package com.rodev.mmf_timetable.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DateStamp(
    val day: Int,
    val month: Int
)

@Serializable
sealed interface NetworkAvailability {

    @Serializable
    @SerialName("after")
    data class After(val date: DateStamp): NetworkAvailability

    @Serializable
    @SerialName("until")
    data class Until(val date: DateStamp): NetworkAvailability

    @Serializable
    @SerialName("in_range")
    data class InRange(val from: DateStamp, val to: DateStamp): NetworkAvailability

    @Serializable
    @SerialName("except")
    data class Except(val dates: List<DateStamp>): NetworkAvailability

    @Serializable
    @SerialName("only")
    data class Only(val dates: List<DateStamp>): NetworkAvailability

    @Serializable
    @SerialName("even_week")
    data object EvenWeek: NetworkAvailability

    @Serializable
    @SerialName("odd_week")
    data object OddWeek: NetworkAvailability
}