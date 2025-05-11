package com.rodev.mmf_timetable.feature.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.data.repository.TeacherRepository
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.Teacher
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import com.rodev.mmf_timetable.feature.teacher.model.getCurrentLessonIn
import com.rodev.mmf_timetable.feature.teacher.model.getTeacherCurrentLessonIn
import com.rodev.mmf_timetable.feature.teacher.navigation.TeacherRoute
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
class TeacherViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    teacherRepository: TeacherRepository,
    lessonRepository: LessonRepository
) : ViewModel() {
    private val teacherId: Long = savedStateHandle.toRoute<TeacherRoute>().teacherId

    val state = teacherUiState(
        teacherFlow = teacherRepository.getTeacherById(teacherId),
        lessonFlow = lessonRepository.getLessonsByTeacherId(teacherId)
    )
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TeacherUiState.Loading
        )
}


@OptIn(ExperimentalCoroutinesApi::class)
private fun teacherUiState(
    teacherFlow: Flow<Teacher?>,
    lessonFlow: Flow<List<Lesson>>
): Flow<TeacherUiState> {
    return teacherFlow
        .combine(lessonFlow) { teacher, lessons -> teacher to lessons }
        .asResult()
        .map {
            when (it) {
                is Result.Exception -> TeacherUiState.Error(it.exception)
                Result.Loading -> TeacherUiState.Loading
                is Result.Success -> {
                    val (teacher, lessons) = it.data
                    if (teacher == null) {
                        TeacherUiState.NotFound
                    } else {
                        val currentLesson = getTeacherCurrentLessonIn(lessons)

                        TeacherUiState.TeacherDetails(
                            teacher = teacher,
                            currentLesson = currentLesson
                        )
                    }
                }
            }
        }
}