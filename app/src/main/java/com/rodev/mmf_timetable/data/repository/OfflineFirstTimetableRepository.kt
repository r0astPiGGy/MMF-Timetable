package com.rodev.mmf_timetable.data.repository

import com.rodev.mmf_timetable.K
import com.rodev.mmf_timetable.data.source.local.TimetableDao
import com.rodev.mmf_timetable.data.source.local.model.LessonEntity
import com.rodev.mmf_timetable.data.source.local.model.TimetableEntity
import com.rodev.mmf_timetable.domain.model.Group
import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.model.TimetableData
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

    override val allGroups: Flow<List<Group>>
        get() = api.allGroups

    override fun getTimetableStream(course: Int, groupId: String): Flow<TimetableData?> {
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
        lessons: List<Lesson>,
        dirty: Boolean = false
    ): TimetableData {
        return TimetableData(
            week = 1, // TODO
            course = course,
            group = groupId,
            lessons = lessons.groupBy { it.weekday },
            dirty = dirty
        )
    }

    private fun mapToTimetableData(entity: TimetableEntity): TimetableData {
        return timetableDataOf(
            course = entity.course,
            groupId = entity.group,
            lessons = entity.allLessons.map(LessonEntity::asExternalModel),
            dirty = System.currentTimeMillis() - entity.createdDate > K.Constants.TIMETABLE_CACHE_INVALIDATION
        )
    }

    private fun mapToEntity(timetableData: TimetableData): TimetableEntity {
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