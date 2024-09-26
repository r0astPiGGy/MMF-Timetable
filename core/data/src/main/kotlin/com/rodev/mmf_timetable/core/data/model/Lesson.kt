package com.rodev.mmf_timetable.core.data.model

import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.core.network.model.NetworkLesson

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
    type = type
)