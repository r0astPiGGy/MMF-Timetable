package com.rodev.mmf_timetable.feature.classroom

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rodev.mmf_timetable.core.data.repository.ClassroomRepository
import com.rodev.mmf_timetable.core.model.data.Classroom
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import com.rodev.mmf_timetable.feature.classroom.navigation.ClassroomRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ClassroomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    classroomRepository: ClassroomRepository,
) : ViewModel() {

    private val classroomId: Long = savedStateHandle.toRoute<ClassroomRoute>().classroomId

    val state = classroomUiState(
        classroomRepository.getClassroomById(classroomId)
    )
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ClassroomUiState.Loading
        )
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun classroomUiState(
    classroomFlow: Flow<Classroom?>
): Flow<ClassroomUiState> {
    return classroomFlow.asResult()
        .map {
            when (it) {
                is Result.Exception -> ClassroomUiState.Error(it.exception)
                Result.Loading -> ClassroomUiState.Loading
                is Result.Success -> {
                    val classroom = it.data
                    if (classroom == null) {
                        ClassroomUiState.NotFound
                    } else {
                        ClassroomUiState.ClassroomDetails(classroom)
                    }
                }
            }
        }
}