package com.rodev.mmf_timetable.data.source.network.model

import com.rodev.mmf_timetable.core.model.data.Group

class Course(
    val course: Int,
    val groups: List<com.rodev.mmf_timetable.core.model.data.Group>
)