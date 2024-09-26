package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.model.data.Lesson
import kotlinx.coroutines.flow.Flow

interface LessonRepository {
    fun getLessons(): Flow<List<Lesson>>

    suspend fun refresh()

}