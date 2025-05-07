package com.rodev.mmf_timetable.feature.preferences.group

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodev.mmf_timetable.core.data.repository.CourseRepository
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class GroupViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    courseRepository: CourseRepository
) : ViewModel() {

    private val selectedCourse = savedStateHandle.getStateFlow("course", 1)

    val state = courseRepository.getCourses()
        .combine(selectedCourse) { courses, course -> course to courses }
        .asResult()
        .map {
            when (it) {
                is Result.Exception -> GroupUiState.Error(it.exception)
                Result.Loading -> GroupUiState.Loading
                is Result.Success -> GroupUiState.Courses(
                    courses = it.data.second,
                    selectedCourse = it.data.first
                )
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GroupUiState.Loading
        )

    fun onCourseChanged(course: Int) {
        savedStateHandle["course"] = course
    }

}
