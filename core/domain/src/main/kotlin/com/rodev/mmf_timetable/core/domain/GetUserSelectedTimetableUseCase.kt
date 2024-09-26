package com.rodev.mmf_timetable.core.domain

import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.model.data.Lesson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSelectedTimetableUseCase @Inject constructor(
    private val repository: LessonRepository
) {

    operator fun invoke(): Flow<List<Lesson>> {
        return repository.getLessons()
    }
}