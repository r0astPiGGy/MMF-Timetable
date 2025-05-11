package com.rodev.mmf_timetable.core.data.model

import com.rodev.mmf_timetable.core.model.data.LessonTeacher
import com.rodev.mmf_timetable.core.model.data.Teacher
import com.rodev.mmf_timetable.core.network.model.NetworkLessonTeacher
import com.rodev.mmf_timetable.core.network.model.NetworkTeacher

fun NetworkTeacher.asExternalModel(): Teacher {
    return Teacher(
        id = id,
        name = name,
        fullName = fullName,
        imageUrl = imageUrl,
        position = position,
        email = email,
        phone = phone
    )
}

fun NetworkLessonTeacher.asExternalModel(): LessonTeacher {
    return LessonTeacher(
        id = id,
        name = name,
        imageUrl = photoUrl,
        fullName = fullName,
        position = position
    )
}