package com.rodev.mmf_timetable.presentation.screen.home_screen.state

import com.rodev.mmf_timetable.domain.service.Course

sealed class HomeScreenEvent {

    object OpenCourseEditDialog : HomeScreenEvent()

    object CloseCourseEditDialog : HomeScreenEvent()

    object FetchTimetable : HomeScreenEvent()

    data class CourseAndGroupSelected(
        val course: Course,
        val group: Course.Group,
        val subGroup: String? = null
    ) : HomeScreenEvent()

}