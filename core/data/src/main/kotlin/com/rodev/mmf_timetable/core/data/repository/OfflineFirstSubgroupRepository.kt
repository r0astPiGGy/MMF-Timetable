package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.data.model.asExternalModel
import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.model.data.SubgroupSubject
import com.rodev.mmf_timetable.core.network.TimetableNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OfflineFirstSubgroupRepository @Inject constructor(
    private val api: TimetableNetworkDataSource
) : SubgroupRepository {
    override fun getSubgroupSubjects(groupId: String): Flow<List<SubgroupSubject>> =
        flow {
            emit(api.getSubgroupSubjects(groupId).map { it.asExternalModel() })
        }

}