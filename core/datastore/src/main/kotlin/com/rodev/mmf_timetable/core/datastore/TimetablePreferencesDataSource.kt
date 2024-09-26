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
            // TODO handle
            if (it.selectedGroup.isEmpty()) return@map null

            UserData(
                course = it.selectedCourse,
                groupId = it.selectedGroup,
                subGroup = if (it.hasSelectedSubGroup()) it.selectedSubGroup else null
            )
        }

    suspend fun updateUserData(userData: UserData) {
        dataStore.updateData {
            it.copy {
                selectedCourse = userData.course
                selectedGroup = userData.groupId
                if (userData.subGroup == null) {
                    clearSelectedSubGroup()
                } else {
                    selectedSubGroup = userData.subGroup!!
                }
            }
        }
    }

    suspend fun setSelectedCourse(course: Int) {
        dataStore.updateData {
            it.copy {
                selectedCourse = course
            }
        }
    }

    suspend fun setSelectedGroup(groupId: String) {
        dataStore.updateData {
            it.copy {
                selectedGroup = groupId
            }
        }
    }

    suspend fun setSelectedSubGroup(subGroup: String?) {
        dataStore.updateData {
            it.copy {
                if (subGroup == null) {
                    clearSelectedSubGroup()
                } else {
                    selectedSubGroup = subGroup
                }
            }
        }
    }

}