package com.rodev.mmf_timetable.core.domain

import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import com.rodev.mmf_timetable.core.model.data.Timetable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetUserSelectedTimetableUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val repository: LessonRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Timetable?> {
        return userDataRepository.userData.flatMapLatest {
            if (it != null) {
                repository.getTimetableStream(it.course, it.groupId)
            } else {
                flowOf(null)
            }
        }
    }
}