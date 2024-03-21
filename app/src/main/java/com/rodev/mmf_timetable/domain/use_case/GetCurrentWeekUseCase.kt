package com.rodev.mmf_timetable.domain.use_case

import com.rodev.mmf_timetable.domain.service.StudyPlanService
import com.rodev.mmf_timetable.utils.CurrentWeekProvider
import java.util.Date

class GetCurrentWeekUseCase(
    private val studyPlanService: StudyPlanService,
    private val getUserInfo: LoadUserInfoUseCase
) {

    suspend operator fun invoke(): Long? {
        val info = getUserInfo() ?: return null

        val course = info.course.course
        val group = info.group.id
        val studyPlan = studyPlanService.getStudyPlan(course, group) ?: return null

        val date = Date()
        val timeSpan = studyPlan.spans.firstOrNull { date.time in it.start..it.end } ?: return null

        val start = Date(timeSpan.start)
        val end = Date(timeSpan.end)

        return CurrentWeekProvider.weekOf(start, end, date)
    }

}