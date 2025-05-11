package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.model.data.Classroom
import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.model.data.Teacher
import kotlinx.coroutines.flow.Flow

interface ClassroomRepository {

    fun getClassrooms(): Flow<List<Classroom>>

    fun getClassroomById(id: Long): Flow<Classroom?>

}