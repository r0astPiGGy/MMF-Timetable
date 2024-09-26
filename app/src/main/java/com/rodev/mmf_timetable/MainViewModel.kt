package com.rodev.mmf_timetable

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import com.rodev.mmf_timetable.core.data.TimetableRepository
import com.rodev.mmf_timetable.core.data.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UserDataUiState {

    @Immutable
    data class Success(
        val course: Int?,
        val group: String?,
        val subGroup: String? = null
    ) : UserDataUiState

    object Loading : UserDataUiState

    object Error : UserDataUiState

}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    timetableRepository: TimetableRepository
) : ViewModel() {


//    @OptIn(ExperimentalCoroutinesApi::class)
//    val state: StateFlow<UserDataUiState> = userDataRepository
//        .userData
//        .combine(timetableRepository.allCourses, ::Pair)
//        .asResult()
//        .mapLatest { userDataToGroups ->
//            when (userDataToGroups) {
//                is Result.Exception -> UserDataUiState.Error
//                Result.Loading -> UserDataUiState.Loading
//                is Result.Success -> {
//                    val (data, groups) = userDataToGroups.data
//
//                    UserDataUiState.Success(
//                        course = data?.course,
//                        group = TODO(),
//                        subGroup = null,
//                    )
//                }
//            }
//        }
//        .stateIn(
//            viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = UserDataUiState.Loading
//        )

    fun updateUserData(course: Int, groupId: String, subGroup: String? = null) {
        viewModelScope.launch {
            userDataRepository.setSelectedCourse(course)
            userDataRepository.setSelectedGroup(groupId)
            userDataRepository.setSelectedSubGroup(subGroup)
        }
    }

}