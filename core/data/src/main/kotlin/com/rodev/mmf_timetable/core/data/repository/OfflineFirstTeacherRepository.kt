package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.data.model.asExternalModel
import com.rodev.mmf_timetable.core.model.data.Teacher
import com.rodev.mmf_timetable.core.network.TimetableNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstTeacherRepository @Inject constructor(
    private val api: TimetableNetworkDataSource
) : TeacherRepository {
    override fun getTeachers(): Flow<List<Teacher>> {
        return flow {
            emit(api.getTeachers().map { it.asExternalModel() })
        }
    }

    override fun getTeacherById(id: Long): Flow<Teacher?> {
        return getTeachers().map { it.firstOrNull { t -> t.id == id } }
    }
}