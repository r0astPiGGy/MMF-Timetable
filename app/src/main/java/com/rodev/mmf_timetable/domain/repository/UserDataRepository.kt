package com.rodev.mmf_timetable.domain.repository

import com.rodev.mmf_timetable.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<com.rodev.mmf_timetable.core.model.data.UserData?>

    suspend fun setSelectedCourse(course: Int)

    suspend fun setSelectedGroup(groupId: String)

    suspend fun setSelectedSubGroup(subGroup: String?)

}