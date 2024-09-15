package com.rodev.mmf_timetable.core.data

import com.rodev.mmf_timetable.core.datastore.TimetablePreferencesDataSource
import com.rodev.mmf_timetable.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineFirstUserDataRepository @Inject constructor(
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