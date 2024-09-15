package com.rodev.mmf_timetable.core.data

import com.rodev.mmf_timetable.core.model.data.TimetableData
import com.rodev.mmf_timetable.core.network.model.NetworkCourse
import kotlinx.coroutines.flow.Flow

interface TimetableRepository {

    val allCourses: Flow<List<NetworkCourse>>

    fun getTimetableStream(course: Int, groupId: String): Flow<TimetableData?>

    suspend fun refresh(course: Int, groupId: String)

}