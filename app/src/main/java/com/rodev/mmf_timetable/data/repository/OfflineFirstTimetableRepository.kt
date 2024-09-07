package com.rodev.mmf_timetable.data.repository

import com.rodev.mmf_timetable.K
import com.rodev.mmf_timetable.data.source.local.TimetableDao
import com.rodev.mmf_timetable.data.source.local.model.LessonEntity
import com.rodev.mmf_timetable.data.source.local.model.TimetableEntity
import com.rodev.mmf_timetable.core.model.data.Group
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.TimetableData
import com.rodev.mmf_timetable.domain.repository.TimetableRepository
import com.rodev.mmf_timetable.domain.service.TimetableNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstTimetableRepository @Inject constructor(
    private val api: TimetableNetworkDataSource,
    private val dao: TimetableDao,
//    private val studyPlanService: StudyPlanService
) : TimetableRepository {
    override val allCourses: Flow<List<Int>>
        get() = api.allCourses

    override val allGroups: Flow<List<com.rodev.mmf_timetable.core.model.data.Group>>
        get() = api.allGroups

    override fun getTimetableStream(course: Int, groupId: String): Flow<com.rodev.mmf_timetable.core.model.data.TimetableData?> {
        return dao.get(TimetableEntity.createId(course, groupId))
            .map { it?.let(::mapToTimetableData) }
    }

    override suspend fun refresh(course: Int, groupId: String) {
        val data = timetableDataOf(
            course = course,
            groupId = groupId,
            lessons = api.getTimetable(course, groupId)
        )

        dao.insert(mapToEntity(data))
    }

    private fun timetableDataOf(
        course: Int,
        groupId: String,
        lessons: List<com.rodev.mmf_timetable.core.model.data.Lesson>,
        dirty: Boolean = false
    ): com.rodev.mmf_timetable.core.model.data.TimetableData {
        return com.rodev.mmf_timetable.core.model.data.TimetableData(
            week = 1, // TODO
            course = course,
            group = groupId,
            lessons = lessons.groupBy { it.weekday },
            dirty = dirty
        )
    }

    private fun mapToTimetableData(entity: TimetableEntity): com.rodev.mmf_timetable.core.model.data.TimetableData {
        return timetableDataOf(
            course = entity.course,
            groupId = entity.group,
            lessons = entity.allLessons.map(LessonEntity::asExternalModel),
            dirty = System.currentTimeMillis() - entity.createdDate > K.Constants.TIMETABLE_CACHE_INVALIDATION
        )
    }

    private fun mapToEntity(timetableData: com.rodev.mmf_timetable.core.model.data.TimetableData): TimetableEntity {
        return TimetableEntity(
            allLessons = timetableData.lessons
                .values
                .flatten()
                .map(LessonEntity::fromExternalModel),
            course = timetableData.course,
            group = timetableData.group,
        )
    }
}