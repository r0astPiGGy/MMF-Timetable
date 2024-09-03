package com.rodev.mmf_timetable.data.source.network.model

import com.rodev.mmf_timetable.domain.model.Group

class Course(
    val course: Int,
    val groups: List<Group>
)