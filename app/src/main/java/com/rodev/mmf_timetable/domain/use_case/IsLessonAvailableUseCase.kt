package com.rodev.mmf_timetable.domain.use_case

import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.utils.available

class IsLessonAvailableUseCase(
    private val loadUserInfo: LoadUserInfoUseCase,
    private val getCurrentWeek: GetCurrentWeekUseCase
) {

    private fun isWeekMatches(weekType: Lesson.WeekType?, week: Long): Boolean {
        return when (weekType) {
            Lesson.WeekType.ODD -> week % 2 == 1L
            Lesson.WeekType.EVEN -> week % 2 == 0L
            null -> true
        }
    }

    suspend operator fun invoke(lesson: Lesson): Boolean {
        val week = getCurrentWeek()

        if (week != null && !isWeekMatches(lesson.week, week)) return false

        val remarks = lesson.remarks ?: return true

        val split = remarks.split(" ")

        if (!split.any { Lesson.subGroups.contains(it) }) {
            return true
        }

        val subGroup = loadUserInfo()?.subGroup ?: return true

        return split.any { it.contains(subGroup) }
    }
}