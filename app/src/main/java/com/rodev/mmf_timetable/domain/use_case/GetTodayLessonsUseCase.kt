package com.rodev.mmf_timetable.domain.use_case

import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.utils.DateUtils

class GetTodayLessonsUseCase(
    private val isLessonAvailable: IsLessonAvailableUseCase
) {

    private fun Lesson.isAvailable(): Boolean {
        return isLessonAvailable(this)
    }

    operator fun invoke(lessons: List<Lesson>): List<Lesson> {
        val weekday = DateUtils.getCurrentWeekday()

        return lessons.filter { it.weekday == weekday && it.isAvailable() }
    }

}