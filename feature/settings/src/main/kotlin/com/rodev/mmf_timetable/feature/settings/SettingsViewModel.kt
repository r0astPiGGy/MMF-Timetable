package com.rodev.mmf_timetable.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.core.data.repository.CourseRepository
import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    lessonRepository: LessonRepository,
    coursesRepository: CourseRepository
) : ViewModel() {
    val state = settingsUiState(
        userDataRepository,
        lessonRepository,
        coursesRepository
    )
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading
        )
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun settingsUiState(
    userDataRepository: UserDataRepository,
    lessonRepository: LessonRepository,
    courseRepository: CourseRepository
): Flow<SettingsUiState> {
    return flowOf(SettingsUiState.Loading)
//    return userDataRepository.userData
//        .distinctUntilChanged()
//        .flatMapLatest { userData ->
//            if (userData == null) {
//                return@flatMapLatest flowOf(SettingsUiState.CourseNotSelected)
//            }
//
//            return@flatMapLatest lessonRepository.getLessons(userData.groupId)
//                .combine(courseRepository.getCourses()) { lessons, courses ->
//                    val selectedGroup = courses.flatMap { it.groups }.firstOrNull { it.id == userData.groupId }
//
//                    lessons to selectedGroup
//                }
//                .asResult()
//                .map { resource ->
//                    when (resource) {
//                        is Result.Exception -> SettingsUiState.Error(resource.exception)
//                        Result.Loading -> SettingsUiState.Loading
//                        is Result.Success -> {
//                            SettingsUiState.Settings(
//                                currentLesson = null,
//                                group = resource.data.second
//                            )
//                        }
//                    }
//                }
//        }
}