package com.rodev.mmf_timetable.core.data.model

import com.rodev.mmf_timetable.core.model.data.Availability
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.WeekType
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.core.network.model.NetworkAvailability
import com.rodev.mmf_timetable.core.network.model.NetworkLesson
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json


private fun parseMinutes(time: String): Int {
    val (hours, minutes) = time.split(":").map { it.toInt() }
    return hours * 60 + minutes
}

fun NetworkLesson.asExternalModel() = Lesson(
    weekday = Weekday.valueOf(weekday),
    id = id,
    subGroup = subgroup?.name,
    subGroupId = subgroup?.id,
    classroom = classroom?.name,
    classroomId = classroom?.id,
    subject = subject ?: "",
    teacher = teachers.firstOrNull()?.name,
    teacherId = teachers.firstOrNull()?.id,
    timeStartMinutes = parseMinutes(timeStart),
    timeEndMinutes = parseMinutes(timeEnd),
    timeStart = timeStart,
    timeEnd = timeEnd,
    type = type,
    availability = null
)

//private fun NetworkAvailability.asExternalModel(): Availability = when(this) {
//    is NetworkAvailability.After -> Availability.After(date, parseWeekType(weekType))
//    is NetworkAvailability.ByWeekType -> Availability.ByWeekType(parseWeekType(weekType)!!)
//    is NetworkAvailability.Except -> Availability.Except(dates, parseWeekType(weekType))
//    is NetworkAvailability.InRange -> Availability.InRange(from, to, parseWeekType(weekType))
//    is NetworkAvailability.Only -> Availability.Only(dates, parseWeekType(weekType))
//    is NetworkAvailability.Until -> Availability.Until(date, parseWeekType(weekType))
//}

private fun parseWeekType(text: String?): WeekType? = text?.let { WeekType.valueOf(it) }