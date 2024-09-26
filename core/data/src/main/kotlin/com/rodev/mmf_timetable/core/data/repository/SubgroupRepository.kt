package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.model.data.SubgroupSubject
import kotlinx.coroutines.flow.Flow

interface SubgroupRepository {

    fun getSubgroupSubjects(groupId: String): Flow<List<SubgroupSubject>>

}