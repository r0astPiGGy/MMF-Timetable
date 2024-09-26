package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.network.TimetableNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OfflineFirstCourseRepository(
    private val api: TimetableNetworkDataSource
) : CourseRepository {
    override fun getCourses(): Flow<List<Course>> = flow {
        api.getCourses()
    }
}