package com.rodev.mmf_timetable.feature.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.data.repository.TeacherRepository
import com.rodev.mmf_timetable.core.domain.isAvailable
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.Teacher
import com.rodev.mmf_timetable.core.result.Result
import com.rodev.mmf_timetable.core.result.asResult
import com.rodev.mmf_timetable.core.ui.weekOf
import com.rodev.mmf_timetable.core.domain.getTeacherCurrentLessonIn
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
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    teacherRepository: TeacherRepository,
    lessonRepository: LessonRepository
) : ViewModel() {
    private val teacherId: Long = savedStateHandle.toRoute<TeacherRoute>().teacherId

    private val selectedDate = savedStateHandle.getStateFlow("date", now().toEpochDays())
        .map(LocalDate::fromEpochDays)

    val state = teacherUiState(
        teacherFlow = teacherRepository.getTeacherById(teacherId),
        lessonFlow = lessonRepository.getLessonsByTeacherId(teacherId),
        dateFlow = selectedDate
    )
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TeacherUiState.Loading
        )

    fun selectDate(date: LocalDate) {
        savedStateHandle["date"] = date.toEpochDays()
    }

    private fun now(): LocalDate {
        return Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
private fun teacherUiState(
    teacherFlow: Flow<Teacher?>,
    lessonFlow: Flow<List<Lesson>>,
    dateFlow: Flow<LocalDate>
): Flow<TeacherUiState> {
    return teacherFlow
        .combine(lessonFlow) { teacher, lessons -> teacher to lessons }
        .combine(dateFlow) { pair, date -> pair to date }
        .asResult()
        .map {
            when (it) {
                is Result.Exception -> TeacherUiState.Error(it.exception)
                Result.Loading -> TeacherUiState.Loading
                is Result.Success -> {
                    val (pair, date) = it.data
                    val (teacher, lessons) = pair

                    if (teacher == null) {
                        TeacherUiState.NotFound
                    } else {
                        val week = weekOf(date)
                        val selectedDate = week.first { it.date == date }

                        val timetable = lessons.groupBy { l -> week.first { it.weekday ==  l.weekday } }
                            .mapValues { (k, v) ->
                                v.map { l ->
                                    AvailableLesson(
                                        isAvailable = l.isAvailable(
                                            date = k.date,
                                        ),
                                        lesson = l
                                    )
                                }.sortedBy { it.lesson.timeStart }
                            }

                        TeacherUiState.TeacherDetails(
                            teacher = teacher,
                            currentLesson = getTeacherCurrentLessonIn(lessons),
                            selectedDate = selectedDate,
                            week = week,
                            timetable = timetable
                        )
                    }
                }
            }
        }
}