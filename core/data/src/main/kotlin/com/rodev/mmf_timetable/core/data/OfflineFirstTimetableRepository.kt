package com.rodev.mmf_timetable.core.data

import com.rodev.mmf_timetable.core.database.TimetableDao
import com.rodev.mmf_timetable.core.database.model.TimetableEntity
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.TimetableData
import com.rodev.mmf_timetable.core.network.TimetableNetworkDataSource
import com.rodev.mmf_timetable.core.network.model.NetworkCourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class OfflineFirstTimetableRepository @Inject constructor(
    private val api: TimetableNetworkDataSource,
    private val dao: TimetableDao
) : TimetableRepository {
    override val allCourses: Flow<List<NetworkCourse>> = flow { api.getCourses() }

    override fun getTimetableStream(course: Int, groupId: String): Flow<TimetableData?> {
        return dao.get(TimetableEntity.createId(course, groupId))
            .map { it?.let(::mapToTimetableData) }
    }

    override suspend fun refresh(course: Int, groupId: String) {
        val data = timetableDataOf(
            course = course,
            groupId = groupId,
            lessons = api.getLessons(groupId) as List<Lesson> // TODO
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
            lessons = entity.allLessons.map(com.rodev.mmf_timetable.core.database.model.LessonEntity::asExternalModel),
            dirty = System.currentTimeMillis() - entity.createdDate > 86400 // TODO
        )
    }

    private fun mapToEntity(timetableData: TimetableData): TimetableEntity {
        return TimetableEntity(
            allLessons = timetableData.lessons
                .values
                .flatten()
                .map(com.rodev.mmf_timetable.core.database.model.LessonEntity::fromExternalModel),
            course = timetableData.course,
            group = timetableData.group,
        )
    }
}