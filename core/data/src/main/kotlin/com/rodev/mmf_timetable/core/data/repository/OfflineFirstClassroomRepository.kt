package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.data.model.asExternalModel
import com.rodev.mmf_timetable.core.model.data.Classroom
import com.rodev.mmf_timetable.core.network.TimetableNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstClassroomRepository @Inject constructor(
    private val api: TimetableNetworkDataSource
) : ClassroomRepository {
    override fun getClassrooms(): Flow<List<Classroom>> {
        return flow {
            emit(api.getClassrooms().map { it.asExternalModel() })
        }
    }

    override fun getClassroomById(id: Long): Flow<Classroom?> {
        return getClassrooms().map { it.firstOrNull { t -> t.id == id } }
    }
}