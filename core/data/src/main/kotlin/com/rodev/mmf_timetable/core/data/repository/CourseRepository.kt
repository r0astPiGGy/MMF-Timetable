package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.model.data.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    fun getCourses(): Flow<List<Course>>

}