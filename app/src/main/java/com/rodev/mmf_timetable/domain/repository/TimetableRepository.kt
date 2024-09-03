package com.rodev.mmf_timetable.domain.repository

import com.rodev.mmf_timetable.domain.model.Group
import com.rodev.mmf_timetable.domain.model.TimetableData
import kotlinx.coroutines.flow.Flow

interface TimetableRepository {

    val allCourses: Flow<List<Int>>

    val allGroups: Flow<List<Group>>

    fun getTimetableStream(course: Int, groupId: String): Flow<TimetableData?>

    suspend fun refresh(course: Int, groupId: String)

}