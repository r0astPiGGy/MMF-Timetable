package com.rodev.mmf_timetable.feature.classrooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.core.data.repository.ClassroomRepository
import com.rodev.mmf_timetable.core.data.repository.CourseRepository
import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ClassroomsViewModel @Inject constructor(
    classroomRepository: ClassroomRepository
) : ViewModel() {
    val state = classroomsUiState(
        classroomRepository
    )
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ClassroomsUiState.Loading
        )
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun classroomsUiState(
    classroomRepository: ClassroomRepository
): Flow<ClassroomsUiState> {
    return classroomRepository.getClassrooms()
        .asResult()
        .map { resource ->
            when (resource) {
                is Result.Exception -> ClassroomsUiState.Error(resource.exception)
                Result.Loading -> ClassroomsUiState.Loading
                is Result.Success -> ClassroomsUiState.Classrooms(resource.data)
            }
        }
}