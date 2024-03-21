package com.rodev.mmf_timetable.domain.service

import com.rodev.mmf_timetable.domain.model.StudyPlan

interface StudyPlanService {

    suspend fun getStudyPlan(course: Int, group: String): StudyPlan?

}