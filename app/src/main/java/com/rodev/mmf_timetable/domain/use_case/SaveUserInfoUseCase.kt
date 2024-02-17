package com.rodev.mmf_timetable.domain.use_case

import android.content.SharedPreferences
import androidx.core.content.edit
import com.rodev.mmf_timetable.domain.model.UserInfo
import com.rodev.mmf_timetable.domain.service.Course
import com.rodev.mmf_timetable.domain.service.TimetableService

private const val GROUP = "group"
private const val COURSE = "course"
private const val SUB_GROUP = "sub_group"

class SaveUserInfoUseCase(
    private val preferences: SharedPreferences
) {

    operator fun invoke(course: Course, group: Course.Group, subGroup: String?) {
        preferences.edit {
            putInt(COURSE, course.course)
            putString(GROUP, group.id)
            putString(SUB_GROUP, subGroup)
        }
    }

}

class LoadUserInfoUseCase(
    private val preferences: SharedPreferences,
    private val service: TimetableService
) {

    operator fun invoke(): UserInfo? {
        val courseId = preferences
            .getInt(COURSE, -1)

        if (courseId == -1) return null

        val groupId = preferences
            .getString(GROUP, null)
            ?: return null

        val subGroup = preferences
            .getString(SUB_GROUP, null)

        val course = service.getCourse(courseId)
        val group = course.getGroup(groupId)

        return UserInfo(course, group, subGroup)
    }

}