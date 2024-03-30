package com.rodev.mmf_timetable.domain.use_case

import com.rodev.mmf_timetable.K
import com.rodev.mmf_timetable.domain.model.Timetable
import com.rodev.mmf_timetable.domain.repository.TimetableRepository
import com.rodev.mmf_timetable.domain.service.ApiResult
import com.rodev.mmf_timetable.domain.service.TimetableService
import com.rodev.mmf_timetable.domain.service.transform

class GetTimetableUseCase(
    private val repository: TimetableRepository,
    private val service: TimetableService,
    private val setLastFetchedTimetable: SetLastFetchedTimetableUseCase
) {

    suspend operator fun invoke(course: Int, group: String): ApiResult<Timetable> {
        val timetable = repository.get(course, group)

        if (timetable != null) {
            setLastFetchedTimetable(timetable.id)

            // If it is valid, not need to update
            if (timetable.isCacheValid()) return ApiResult.Success(timetable)
        }

        // Else load new timetable
        return service
            .getCourse(course)
            .getGroup(group)
            .getTimetable()
            .transform { lessons ->
                Timetable(
                    allLessons = lessons,
                    course = course,
                    group = group
                ).also {
                    setLastFetchedTimetable(it.id)
                    repository.insert(it)
                }
            }
    }

    private fun Timetable.isCacheValid(): Boolean {
        return System.currentTimeMillis() - createdDate <= K.Constants.TIMETABLE_CACHE_INVALIDATION
    }

}