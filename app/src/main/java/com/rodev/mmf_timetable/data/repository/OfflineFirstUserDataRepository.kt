package com.rodev.mmf_timetable.data.repository

import com.rodev.mmf_timetable.data.TimetablePreferencesDataSource
import com.rodev.mmf_timetable.domain.model.UserData
import com.rodev.mmf_timetable.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class OfflineFirstUserDataRepository @Inject constructor(
    private val preferencesDataStore: TimetablePreferencesDataSource
) : UserDataRepository {

    override val userData: Flow<UserData?> = preferencesDataStore.userData

    override suspend fun setSelectedCourse(course: Int) {
        preferencesDataStore.setSelectedCourse(course)
    }

    override suspend fun setSelectedGroup(groupId: String) {
        preferencesDataStore.setSelectedGroup(groupId)
    }

    override suspend fun setSelectedSubGroup(subGroup: String?) {
        preferencesDataStore.setSelectedSubGroup(subGroup)
    }
}