package com.rodev.mmf_timetable.data.service

import com.rodev.mmf_timetable.core.model.data.StudyPlan
import com.rodev.mmf_timetable.core.model.data.TimeSpan
import com.rodev.mmf_timetable.domain.service.StudyPlanService
import com.rodev.mmf_timetable.utils.CurrentWeekProvider

class StudyPlanServiceImpl : StudyPlanService {

    private val studyPlan = com.rodev.mmf_timetable.core.model.data.StudyPlan(
        listOf(
            com.rodev.mmf_timetable.core.model.data.TimeSpan(
                type = "practice",
                start = CurrentWeekProvider.millisOf(5, 1),
                end = CurrentWeekProvider.millisOf(8, 5),
            )
        )
    )

    override suspend fun getStudyPlan(course: Int, group: String): com.rodev.mmf_timetable.core.model.data.StudyPlan {
        return studyPlan
    }
}