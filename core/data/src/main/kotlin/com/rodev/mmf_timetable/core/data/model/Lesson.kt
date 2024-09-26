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

fun NetworkLesson.asExternalModel() = Lesson(
    weekday = Weekday.valueOf(weekday),
    id = id,
    subGroup = subgroupName,
    subGroupId = subgroup,
    classroom = classroomName,
    classroomId = classroom,
    subject = subject ?: "",
    teacher = teacherName,
    teacherId = teacher,
    timeStartMinutes = timeStart,
    timeEndMinutes = timeEnd,
    type = type,
    availability = availability?.asExternalModel()
)

private fun NetworkAvailability.asExternalModel(): Availability = when(this) {
    is NetworkAvailability.After -> Availability.After(date, parseWeekType(weekType))
    is NetworkAvailability.ByWeekType -> Availability.ByWeekType(parseWeekType(weekType)!!)
    is NetworkAvailability.Except -> Availability.Except(dates, parseWeekType(weekType))
    is NetworkAvailability.InRange -> Availability.InRange(from, to, parseWeekType(weekType))
    is NetworkAvailability.Only -> Availability.Only(dates, parseWeekType(weekType))
    is NetworkAvailability.Until -> Availability.Until(date, parseWeekType(weekType))
}

private fun parseWeekType(text: String?): WeekType? = text?.let { WeekType.valueOf(it) }