package com.rodev.mmf_timetable.domain.use_case

import com.rodev.mmf_timetable.domain.model.Timetable
import com.rodev.mmf_timetable.domain.repository.TimetableRepository
import com.rodev.mmf_timetable.domain.service.ApiResult
import com.rodev.mmf_timetable.domain.service.TimetableService
import com.rodev.mmf_timetable.domain.service.transform

class FetchTimetableUseCase(
    private val timetableRepository: TimetableRepository,
    private val timetableService: TimetableService,
    private val setLastFetchedTimetableUseCase: SetLastFetchedTimetableUseCase,
) {

    suspend operator fun invoke(course: Int, group: String): ApiResult<Timetable> {
        return timetableService
            .getCourse(course)
            .getGroup(group)
            .getTimetable()
            .transform { lessons ->
                Timetable(
                    allLessons = lessons,
                    course = course,
                    group = group
                ).also {
                    setLastFetchedTimetableUseCase(it.id)
                    timetableRepository.insert(it)
                }
            }
    }
}