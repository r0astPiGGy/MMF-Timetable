package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.model.data.Teacher
import kotlinx.coroutines.flow.Flow

interface TeacherRepository {

    fun getTeachers(): Flow<List<Teacher>>

    fun getTeacherById(id: Long): Flow<Teacher?>

}