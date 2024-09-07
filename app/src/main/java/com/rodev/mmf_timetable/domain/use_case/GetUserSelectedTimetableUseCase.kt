package com.rodev.mmf_timetable.domain.use_case

import com.rodev.mmf_timetable.core.model.data.TimetableData
import com.rodev.mmf_timetable.domain.repository.TimetableRepository
import com.rodev.mmf_timetable.domain.repository.UserDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetUserSelectedTimetableUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val repository: TimetableRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<com.rodev.mmf_timetable.core.model.data.TimetableData?> {
        return userDataRepository.userData.flatMapLatest {
            if (it != null) {
                repository.getTimetableStream(it.course, it.groupId)
            } else {
                flowOf(null)
            }
        }
    }
}