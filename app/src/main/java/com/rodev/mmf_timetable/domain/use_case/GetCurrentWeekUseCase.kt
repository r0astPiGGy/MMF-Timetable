package com.rodev.mmf_timetable.domain.use_case

import com.rodev.mmf_timetable.domain.repository.UserDataRepository
import com.rodev.mmf_timetable.domain.service.StudyPlanService
import com.rodev.mmf_timetable.utils.CurrentWeekProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class GetCurrentWeekUseCase @Inject constructor(
    private val studyPlanService: StudyPlanService,
    private val userDataRepository: UserDataRepository
) {

    suspend operator fun invoke(): Flow<Long?> = userDataRepository.userData.map { userData ->
        if (userData == null) return@map null

        val studyPlan = studyPlanService
            .getStudyPlan(userData.course, userData.groupId) ?: return@map null

        val date = Date()
        val timeSpan = studyPlan.spans
            .firstOrNull { date.time in it.start..it.end } ?: return@map null

        val start = Date(timeSpan.start)
        val end = Date(timeSpan.end)

        return@map CurrentWeekProvider.weekOf(start, end, date)
    }

}