package com.rodev.mmf_timetable.domain.repository

import com.rodev.mmf_timetable.core.model.data.Group
import com.rodev.mmf_timetable.core.model.data.TimetableData
import kotlinx.coroutines.flow.Flow

interface TimetableRepository {

    val allCourses: Flow<List<Int>>

    val allGroups: Flow<List<com.rodev.mmf_timetable.core.model.data.Group>>

    fun getTimetableStream(course: Int, groupId: String): Flow<com.rodev.mmf_timetable.core.model.data.TimetableData?>

    suspend fun refresh(course: Int, groupId: String)

}