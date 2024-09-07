package com.rodev.mmf_timetable.domain.service

import com.rodev.mmf_timetable.core.model.data.StudyPlan

interface StudyPlanService {

    suspend fun getStudyPlan(course: Int, group: String): com.rodev.mmf_timetable.core.model.data.StudyPlan?

}