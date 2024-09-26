package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData?>

    suspend fun updateUserData(userData: UserData)

}