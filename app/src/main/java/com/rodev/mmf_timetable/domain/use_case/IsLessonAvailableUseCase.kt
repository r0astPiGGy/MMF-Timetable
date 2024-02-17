package com.rodev.mmf_timetable.domain.use_case

import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.utils.available

class IsLessonAvailableUseCase(
    private val loadUserInfo: LoadUserInfoUseCase
) {

    operator fun invoke(lesson: Lesson): Boolean {
        if (!lesson.available) return false

        val remarks = lesson.remarks ?: return true

        val split = remarks.split(" ")

        if (!split.any { Lesson.subGroups.contains(it) }) {
            return true
        }

        val subGroup = loadUserInfo()?.subGroup ?: return true

        return split.any { it.contains(subGroup) }
    }
}