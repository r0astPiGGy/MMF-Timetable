package com.rodev.mmf_timetable.feature.preferences.subgroup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rodev.mmf_timetable.core.data.repository.CourseRepository
import com.rodev.mmf_timetable.core.data.repository.SubgroupRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import com.rodev.mmf_timetable.core.model.data.UserData
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class SubgroupViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository,
    subgroupRepository: SubgroupRepository
) : ViewModel() {
    private val groupId = savedStateHandle.toRoute<SubgroupRoute>().groupId

    val state = subgroupRepository.getSubgroupSubjects(groupId)
        .asResult()
        .map {
            when (it) {
                is Result.Exception -> SubgroupUiState.Error(it.exception)
                Result.Loading -> SubgroupUiState.Loading
                is Result.Success -> SubgroupUiState.Subjects(
                    it.data
                )
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SubgroupUiState.Loading
        )

    suspend fun updateData(subgroupIds: Set<Long>) = withContext(viewModelScope.coroutineContext) {
        userDataRepository.updateUserData(UserData(
            groupId = groupId,
            subgroups = subgroupIds
        ))
    }

}
