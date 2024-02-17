package com.rodev.mmf_timetable.domain.use_case

import com.rodev.mmf_timetable.domain.service.Course
import com.rodev.mmf_timetable.domain.service.TimetableService

class GetCoursesUseCase(
    private val timetableService: TimetableService
) {

    operator fun invoke(): List<Course> {
        return timetableService.courses
    }
}