package com.rodev.mmf_timetable.domain.model

import com.rodev.mmf_timetable.domain.service.Course

data class UserInfo(
    val course: Course,
    val group: Course.Group,
    val subGroup: String?
)
