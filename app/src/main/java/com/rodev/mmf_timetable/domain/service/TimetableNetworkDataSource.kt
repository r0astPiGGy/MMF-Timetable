package com.rodev.mmf_timetable.domain.service

import com.rodev.mmf_timetable.core.model.data.Group
import com.rodev.mmf_timetable.core.model.data.Lesson
import kotlinx.coroutines.flow.Flow

interface TimetableNetworkDataSource {

    val allCourses: Flow<List<Int>>

    val allGroups: Flow<List<Group>>

    suspend fun getTimetable(course: Int, groupId: String): List<Lesson>

}