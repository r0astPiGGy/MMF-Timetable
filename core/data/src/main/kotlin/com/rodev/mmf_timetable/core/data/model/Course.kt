package com.rodev.mmf_timetable.core.data.model

import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.model.data.Group
import com.rodev.mmf_timetable.core.network.model.NetworkCourse

fun NetworkCourse.asExternalModel() = Course(
    course = course,
    groups = groups.map { it.asExternalModel(course) }
)