package com.rodev.mmf_timetable.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private inline fun <reified T> T.encode(): String =
    Json.encodeToString(this).let { "\'$it\'" }

@Serializable
sealed interface NetworkAvailability {

    @Serializable
    @SerialName("AFTER")
    data class After(val date: LocalDate, val weekType: String?): NetworkAvailability {
        override fun toString() = encode()
    }

    @Serializable
    @SerialName("UNTIL")
    data class Until(val date: LocalDate, val weekType: String?): NetworkAvailability {
        override fun toString() = encode()
    }

    @Serializable
    @SerialName("IN_RANGE")
    data class InRange(val from: LocalDate, val to: LocalDate, val weekType: String?): NetworkAvailability {
        override fun toString() = encode()
    }

    @Serializable
    @SerialName("EXCEPT")
    data class Except(val dates: List<LocalDate>, val weekType: String?): NetworkAvailability {
        override fun toString() = encode()
    }

    @Serializable
    @SerialName("ONLY")
    data class Only(val dates: List<LocalDate>, val weekType: String?): NetworkAvailability {
        override fun toString() = encode()
    }

    @Serializable
    @SerialName("BY_WEEK_TYPE")
    data class ByWeekType(val weekType: String): NetworkAvailability {
        override fun toString() = encode()
    }
}