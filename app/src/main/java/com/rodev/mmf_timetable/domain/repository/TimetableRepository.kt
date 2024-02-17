package com.rodev.mmf_timetable.domain.repository

import com.rodev.mmf_timetable.domain.model.Timetable

interface TimetableRepository {

    suspend fun get(course: Int, group: String): Timetable?

    suspend fun get(id: String): Timetable?

    suspend fun insert(timetable: Timetable)

    suspend fun delete(course: Int, group: String)

}