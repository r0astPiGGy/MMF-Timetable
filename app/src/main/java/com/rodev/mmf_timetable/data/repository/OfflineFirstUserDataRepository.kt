package com.rodev.mmf_timetable.data.repository

import com.rodev.mmf_timetable.core.model.data.UserData
import com.rodev.mmf_timetable.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineFirstUserDataRepository @Inject constructor(
    private val preferencesDataStore: com.rodev.mmf_timetable.core.datastore.TimetablePreferencesDataSource
) : UserDataRepository {

    override val userData: Flow<com.rodev.mmf_timetable.core.model.data.UserData?> = preferencesDataStore.userData

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