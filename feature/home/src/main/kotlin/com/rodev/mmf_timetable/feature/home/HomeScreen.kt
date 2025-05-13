package com.rodev.mmf_timetable.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.SensorDoor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rodev.mmf_timetable.core.domain.CurrentLesson
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.ui.ItemDetailsBottomSheet
import com.rodev.mmf_timetable.core.ui.NextLessonCard
import com.rodev.mmf_timetable.core.ui.OngoingLessonCard
import com.rodev.mmf_timetable.core.ui.TeacherRow
import com.rodev.mmf_timetable.core.ui.rememberItemDetailsBottomSheetState

@Composable
internal fun HomeRoute(
    modifier: Modifier = Modifier,
    onGotoTeacher: (Long) -> Unit,
    onGotoRoom: (Long) -> Unit,
    onGotoTeachers: () -> Unit,
    onGotoRooms: () -> Unit,
    onChangeGroup: () -> Unit,
    onAddWidget: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        state = state,
        onAddWidget = onAddWidget,
        onGotoRooms = onGotoRooms,
        onChangeGroup = onChangeGroup,
        onGotoTeachers = onGotoTeachers,
        onGotoTeacher = onGotoTeacher,
        onGotoRoom = onGotoRoom
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    onGotoTeachers: () -> Unit,
    onGotoTeacher: (Long) -> Unit,
    onGotoRoom: (Long) -> Unit,
    onGotoRooms: () -> Unit,
    onChangeGroup: () -> Unit,
    onAddWidget: () -> Unit,
    state: HomeUiState
) {
    HomeScreenScaffold(
        modifier = modifier,
        lesson = {
            if (state is HomeUiState.Home) {
                val currentLesson = state.currentLesson
                val sheetState = rememberItemDetailsBottomSheetState<Lesson>()

                ItemDetailsBottomSheet(state = sheetState) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "Предмет",
                                style = MaterialTheme.typography.labelMedium,
                            )
                            Text(
                                text = it.subject
                            )
                        }
                        val teachers = it.teachers
                        if (teachers.isNotEmpty()) {
                            HorizontalDivider()
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = if (teachers.size == 1) "Преподаватель" else "Преподаватели",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                teachers.forEach { t ->
                                    TeacherRow(
                                        teacher = t,
                                        onGotoTeacher = {
                                            sheetState.close()
                                            onGotoTeacher(it)
                                        }
                                    )
                                }
                            }
                        }
                        val room = it.classroom
                        if (room != null) {
                            HorizontalDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .clickable(onClick = { onGotoRoom(room.id) })
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Аудитория",
                                        style = MaterialTheme.typography.labelMedium,
                                    )
                                    Text(room.name)
                                }
                                Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = null)
                            }
                        }
                    }
                }

                when (currentLesson) {
                    is CurrentLesson.Current -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Текущая пара",
                                style = MaterialTheme.typography.labelLarge
                                    .merge(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            OngoingLessonCard(
                                lesson = currentLesson.lesson,
                                remaining = currentLesson.remaining,
                                progress = currentLesson.progress,
                                courseGroup = {},
                                onClick = { sheetState.open(currentLesson.lesson) }
                            )
                        }
                    }

                    is CurrentLesson.Next -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Следующая пара",
                                style = MaterialTheme.typography.labelLarge
                                    .merge(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            NextLessonCard(
                                lesson = currentLesson.lesson,
                                remaining = currentLesson.remaining,
                                courseGroup = {},
                                onClick = { sheetState.open(currentLesson.lesson) }
                            )
                        }
                    }

                    CurrentLesson.NoLessonToday -> {

                    }
                }
            }
        },
        group = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
//                    verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.group),
                        style = MaterialTheme.typography.labelLarge
                            .merge(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    TextButton(onClick = onChangeGroup) {
                        Text(text = stringResource(R.string.edit))
                    }
                }
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        onClick = onChangeGroup
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 12.dp),
                        ) {
                            when (state) { // Current lesson / group
                                is HomeUiState.Error -> ErrorState(
                                    text = state.exception.stackTraceToString()
                                )

                                HomeUiState.Loading -> LoadingState()
                                is HomeUiState.Home -> {
                                    Text(
                                        text = stringResource(
                                            R.string.course_group,
                                            state.group?.course ?: 0,
                                            state.group?.name.toString()
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    if (state.group?.description != null) {
                                        Text(
                                            text = state.group.description.toString(),
                                            modifier = Modifier.fillMaxWidth(),
                                            style = MaterialTheme.typography.bodySmall.merge(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        )
                                    }
                                }

                                HomeUiState.CourseNotSelected -> {
                                    Text("Курс и группа не выбраны")
                                }
                            }
                        }
                    }
                }
            }
        },
        rooms = {
            OutlinedCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                onClick = onGotoTeachers
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Filled.Person, contentDescription = "Teachers")
                    Text(
                        text = stringResource(R.string.teachers)
                    )
                }
            }
        },
        teachers = {
            OutlinedCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                onClick = onGotoRooms
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.SensorDoor, contentDescription = "Rooms")
                    Text(
                        text = stringResource(R.string.rooms)
                    )
                }
            }
        },
        addWidget = {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                onClick = onAddWidget
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.add_widget),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.size(16.dp))
                    Icon(
                        Icons.Outlined.AddBox,
                        contentDescription = "Add widget",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    )
}

@Composable
private fun HomeScreenScaffold(
    lesson: @Composable () -> Unit,
    group: @Composable () -> Unit,
    rooms: @Composable RowScope.() -> Unit,
    teachers: @Composable RowScope.() -> Unit,
    addWidget: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(Modifier.size(12.dp))
        lesson()
        Spacer(Modifier.size(4.dp))
        group()
        Spacer(Modifier.size(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            teachers()
            rooms()
        }
        Spacer(Modifier.size(12.dp))
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            addWidget()
        }
    }
}

@Composable
private fun CourseNotSelectedState(modifier: Modifier = Modifier) {

}

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(modifier = modifier, text = text)
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Text("Загрузка...")
}
