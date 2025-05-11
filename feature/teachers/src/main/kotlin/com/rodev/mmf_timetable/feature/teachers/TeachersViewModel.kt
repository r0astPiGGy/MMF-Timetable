package com.rodev.mmf_timetable.feature.teachers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.core.data.repository.TeacherRepository
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TeachersViewModel @Inject constructor(
    teacherRepository: TeacherRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val query = savedStateHandle.getStateFlow("query", "")

    val state = teachersUiState(
        teacherRepository = teacherRepository,
        queryFlow = query
    )
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TeachersUiState.Loading
        )

    fun onQueryChange(str: String) {
        savedStateHandle["query"] = str
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun teachersUiState(
    teacherRepository: TeacherRepository,
    queryFlow: Flow<String>
): Flow<TeachersUiState> {
    return teacherRepository.getTeachers()
        .combine(queryFlow) { teachers, query -> teachers to query }
        .asResult()
        .map { resource ->
            when (resource) {
                is Result.Exception -> TeachersUiState.Error(resource.exception)
                Result.Loading -> TeachersUiState.Loading
                is Result.Success -> {
                    val (teachers, query) = resource.data
                    val filtered = teachers.filter {
                        val name = it.fullName ?: it.name
                        name.contains(query, ignoreCase = true)
                    }
                    when {
                        filtered.isEmpty() && query.isEmpty() -> TeachersUiState.Empty
                        filtered.isEmpty() && query.isNotEmpty() -> TeachersUiState.NothingFoundByQuery
                        else -> TeachersUiState.Teachers(filtered)
                    }
                }
            }
        }
}