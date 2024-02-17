package com.rodev.mmf_timetable.domain.service

import com.rodev.mmf_timetable.domain.model.Lesson

interface TimetableService {

    val courses: List<Course>

    fun getCourse(course: Int): Course

}

interface Course {

    val course: Int
    val groupList: List<Group>

    fun getGroup(id: String): Group

    interface Group {

        val name: String
        val id: String

        suspend fun getTimetable(): ApiResult<List<Lesson>>

    }

}