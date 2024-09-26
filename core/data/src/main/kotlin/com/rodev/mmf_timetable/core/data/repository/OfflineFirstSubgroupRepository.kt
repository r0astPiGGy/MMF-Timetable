package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.model.data.SubgroupSubject
import com.rodev.mmf_timetable.core.network.TimetableNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OfflineFirstSubgroupRepository(
    private val api: TimetableNetworkDataSource
) : SubgroupRepository {
    override fun getSubgroupSubjects(groupId: String): Flow<List<SubgroupSubject>> =
        flow { api.getSubgroupSubjects(groupId) }

}