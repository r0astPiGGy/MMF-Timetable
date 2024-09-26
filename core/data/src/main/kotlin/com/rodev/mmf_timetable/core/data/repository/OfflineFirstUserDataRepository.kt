package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.datastore.TimetablePreferencesDataSource
import com.rodev.mmf_timetable.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineFirstUserDataRepository @Inject constructor(
    private val preferencesDataStore: TimetablePreferencesDataSource
) : UserDataRepository {

    override val userData: Flow<UserData?> = preferencesDataStore.userData

    override suspend fun updateUserData(userData: UserData) {
        preferencesDataStore.updateUserData(userData)
    }
}