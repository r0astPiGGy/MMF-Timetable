package com.rodev.mmf_timetable.core.data.repository

import com.rodev.mmf_timetable.core.data.model.asExternalModel
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.network.TimetableNetworkDataSource
import com.rodev.mmf_timetable.core.network.model.NetworkLesson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class OfflineFirstLessonRepository @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val api: TimetableNetworkDataSource
) : LessonRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLessons(): Flow<List<Lesson>> =
        userDataRepository.userData.distinctUntilChanged()
            .flatMapLatest { userData ->
                if (userData == null) {
                    flowOf(emptyList())
                } else {
                    flow { api.getLessons(userData.groupId).map(NetworkLesson::asExternalModel) }
                }
            }

    override suspend fun refresh() {

    }
}