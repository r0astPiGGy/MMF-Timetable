package com.rodev.mmf_timetable.core.data.model

import com.rodev.mmf_timetable.core.model.data.Availability
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.WeekType
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.core.network.model.DateStamp
import com.rodev.mmf_timetable.core.network.model.NetworkAvailability
import com.rodev.mmf_timetable.core.network.model.NetworkLesson
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

fun NetworkLesson.asExternalModel() = Lesson(
    weekday = Weekday.valueOf(weekday),
    id = id,
    group = group?.asExternalModel(),
    subGroup = subgroup?.asExternalModel(),
    classroom = classroom?.asExternalModel(),
    subject = subject ?: "",
    teachers = teachers.map { it.asExternalModel() },
    timeStart = LocalTime.parse(timeStart),
    timeEnd = LocalTime.parse(timeEnd),
    type = type,
    availability = availability.map { it.asExternalModel() },
    additionalInfo = additionalInfo
)

private fun DateStamp.toLocalDate(): LocalDate {
    val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val year = date.year
    return LocalDate(year = year, month = Month.of(month), dayOfMonth = day)
}

private fun NetworkAvailability.asExternalModel(): Availability = when(this) {
    is NetworkAvailability.After -> Availability.After(date = date.toLocalDate())
    NetworkAvailability.EvenWeek -> Availability.EvenWeek
    is NetworkAvailability.Except -> Availability.Except(dates = dates.map { it.toLocalDate() })
    is NetworkAvailability.InRange -> Availability.InRange(from = from.toLocalDate(), to = to.toLocalDate())
    NetworkAvailability.OddWeek -> Availability.OddWeek
    is NetworkAvailability.Only -> Availability.Only(dates = dates.map { it.toLocalDate() })
    is NetworkAvailability.Until -> Availability.Until(date = date.toLocalDate())
}

private fun parseWeekType(text: String?): WeekType? = text?.let { WeekType.valueOf(it) }