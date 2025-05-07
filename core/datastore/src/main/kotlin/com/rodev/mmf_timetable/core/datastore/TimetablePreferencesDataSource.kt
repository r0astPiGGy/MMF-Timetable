package com.rodev.mmf_timetable.core.datastore

import androidx.datastore.core.DataStore
import com.rodev.mmf_timetable.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimetablePreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<UserPreferences>
) {

    val userData: Flow<UserData?> = dataStore.data
        .map {
            UserData(
                groupId = it.group,
                subgroups = it.subgroupsList.toSet(),
            )
        }

    suspend fun updateUserData(userData: UserData) {
        dataStore.updateData {
            it.copy {
                group = userData.groupId
                this.subgroups.clear()
                this.subgroups.addAll(userData.subgroups)
            }
        }
    }

    suspend fun setSelectedGroup(groupId: String) {
        dataStore.updateData {
            it.copy {
                group = groupId
            }
        }
    }

    suspend fun setSelectedSubGroups(subgroups: Set<Long>) {
        dataStore.updateData {
            it.copy {
                this.subgroups.clear()
                this.subgroups.addAll(subgroups)
            }
        }
    }

}