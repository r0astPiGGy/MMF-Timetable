package com.rodev.mmf_timetable.data.repository

import com.rodev.mmf_timetable.data.source.TimetableDao
import com.rodev.mmf_timetable.domain.model.Timetable
import com.rodev.mmf_timetable.domain.repository.TimetableRepository

class TimetableRepositoryImpl(
    private val timetableDao: TimetableDao
) : TimetableRepository {
    override suspend fun get(course: Int, group: String): Timetable? {
        return get(Timetable.createId(course, group))
    }

    override suspend fun get(id: String): Timetable? {
        return timetableDao.get(id)
    }

    override suspend fun insert(timetable: Timetable) {
        timetableDao.insert(timetable)
    }

    override suspend fun delete(course: Int, group: String) {
        TODO("Not yet implemented")
    }
}