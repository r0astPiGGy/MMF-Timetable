package com.rodev.mmf_timetable.feature.teacher

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.Teacher
import com.rodev.mmf_timetable.core.ui.DatePickerModal
import com.rodev.mmf_timetable.core.ui.DynamicScaffoldPortal
import com.rodev.mmf_timetable.core.ui.HorizontalPagerAdapter
import com.rodev.mmf_timetable.core.ui.ItemDetailsBottomSheet
import com.rodev.mmf_timetable.core.ui.LessonCard
import com.rodev.mmf_timetable.core.ui.PagerValuesState
import com.rodev.mmf_timetable.core.ui.SelectedMonthRow
import com.rodev.mmf_timetable.core.ui.TeacherNextLessonCard
import com.rodev.mmf_timetable.core.ui.TeacherOngoingLessonCard
import com.rodev.mmf_timetable.core.ui.TeacherRow
import com.rodev.mmf_timetable.core.ui.WeekDaySelectRow
import com.rodev.mmf_timetable.core.ui.rememberItemDetailsBottomSheetState
import com.rodev.mmf_timetable.feature.teacher.model.CurrentLesson
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
internal fun TeacherRoute(
    modifier: Modifier = Modifier,
    viewModel: TeacherViewModel = hiltViewModel(),
    onGotoRoom: (Long) -> Unit,
    onGotoTeacher: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TeacherScreen(
        modifier = modifier,
        state = state,
        onDateSelect = viewModel::selectDate,
        onGotoTeacher = onGotoTeacher,
        onGotoRoom = onGotoRoom
    )
}

@Composable
internal fun TeacherScreen(
    state: TeacherUiState,
    onDateSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    onGotoRoom: (Long) -> Unit,
    onGotoTeacher: (Long) -> Unit,
) {
    DynamicScaffoldPortal(title = stringResource(R.string.teacher_title))

    Column(modifier = modifier) {
        when (state) {
            is TeacherUiState.Error -> ErrorState(
                text = state.exception.stackTraceToString()
            )

            TeacherUiState.Loading -> LoadingState()
            is TeacherUiState.TeacherDetails -> {
                TeacherDetails(
                    state = state,
                    modifier = modifier,
                    onDateSelect = onDateSelect,
                    onGotoRoom = onGotoRoom,
                    onGotoTeacher = onGotoTeacher
                )
            }

            TeacherUiState.NotFound -> NotFoundState()
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun TeacherDetails(
    state: TeacherUiState.TeacherDetails,
    modifier: Modifier = Modifier,
    onDateSelect: (LocalDate) -> Unit,
    onGotoRoom: (Long) -> Unit,
    onGotoTeacher: (Long) -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val screenHeight = maxHeight
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            val teacher = state.teacher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = teacher.imageUrl,
                        contentDescription = "Teacher image",
                        contentScale = ContentScale.Crop,
                    )
                }
                val clipboard = LocalClipboard.current
                val coroutineScope = rememberCoroutineScope()

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = teacher.fullName ?: teacher.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .basicMarquee()
                    )
                    val email = teacher.email
                    if (!email.isNullOrEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {
                                    coroutineScope.launch {
                                        val data = ClipData.newPlainText("email", email)
                                        val clipEntry = ClipEntry(data)
                                        clipboard.setClipEntry(clipEntry)
                                    }
                                })
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Email, contentDescription = "email")
                            Spacer(Modifier.size(4.dp))
                            Text(
                                text = email,
                                modifier = Modifier
                                    .weight(1f)
                                    .basicMarquee()
                            )
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "copy",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    val phone = teacher.phone
                    if (!phone.isNullOrEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {
                                    coroutineScope.launch {
                                        val data = ClipData.newPlainText("phone", phone)
                                        val clipEntry = ClipEntry(data)
                                        clipboard.setClipEntry(clipEntry)
                                    }
                                })
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "email")
                            Spacer(Modifier.size(4.dp))
                            Text(
                                text = phone,
                                modifier = Modifier
                                    .weight(1f)
                                    .basicMarquee()
                            )
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "copy",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val currentLesson = state.currentLesson
                when (currentLesson) {
                    is CurrentLesson.Current -> {
                        Text(
                            text = "Текущая пара",
                            style = MaterialTheme.typography.labelLarge
                        )
                        TeacherOngoingLessonCard(
                            lesson = currentLesson.lesson,
                            remaining = currentLesson.remaining,
                            progress = currentLesson.progress
                        )
                    }

                    is CurrentLesson.Next -> {
                        Text(
                            text = "Следующая пара",
                            style = MaterialTheme.typography.labelLarge
                        )
                        TeacherNextLessonCard(
                            lesson = currentLesson.lesson,
                            remaining = currentLesson.remaining,
                        )
                    }

                    CurrentLesson.NoLessonToday -> {

                    }
                }
            }

            Column(modifier = Modifier.height(screenHeight)) {
                TeacherLessons(
                    state = state,
                    onDateSelect = onDateSelect,
                    scrollState = scrollState,
                    onGotoTeacher = onGotoTeacher,
                    onGotoRoom = onGotoRoom
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeacherLessons(
    state: TeacherUiState.TeacherDetails,
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    onDateSelect: (LocalDate) -> Unit,
    onGotoRoom: (Long) -> Unit,
    onGotoTeacher: (Long) -> Unit,
) {
    val pagerState =
        rememberPagerState(initialPage = state.week.indexOf(state.selectedDate)) { state.week.size }

    LaunchedEffect(state.week) {
        val index = state.week.indexOf(state.selectedDate)

        if (index >= 0 && pagerState.currentPage != index) {
            pagerState.scrollToPage(index)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (state.week[pagerState.currentPage] != state.selectedDate) {
            onDateSelect(state.week[pagerState.currentPage].date)
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val pagerValues = remember(state.week) { PagerValuesState(state.week) }

    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { it?.let(onDateSelect) },
            onDismiss = { showDatePicker = false })
    }


    val sheetState = rememberItemDetailsBottomSheetState<AvailableLesson>()

    ItemDetailsBottomSheet(state = sheetState) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Предмет",
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = it.lesson.subject
                )
            }
            val teachers = it.lesson.teachers
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
            val room = it.lesson.classroom
            if (room != null) {
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                sheetState.close()
                                onGotoRoom(room.id)
                            }
                        )
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

            val group = it.lesson.group
            if (group != null) {
                HorizontalDivider()
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Группа",
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Text(
                        text = stringResource(
                            com.rodev.mmf_timetable.core.ui.R.string.ui_course_group,
                            group.course,
                            group.name
                        )
                    )
                    val description = group.description
                    if (description != null) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall.merge(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            }
        }
    }


    Column(
        modifier = modifier
    ) {
        SelectedMonthRow(
            selectedDate = state.selectedDate.date,
            onDayPickerSelect = { showDatePicker = true }
        )
        WeekDaySelectRow(
            selectedDate = state.selectedDate,
            week = state.week,
            onTabSelect = {
                if (state.selectedDate != it) {
                    coroutineScope.launch {
                        pagerState.scrollToPage(state.week.indexOf(it))
                    }
                }
            }
        )
        HorizontalPagerAdapter(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .nestedScroll(remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource
                        ): Offset {
                            return if (available.y > 0) Offset.Zero else Offset(
                                x = 0f,
                                y = -scrollState.dispatchRawDelta(-available.y)
                            )
                        }
                    }
                }),
            state = pagerState,
            valuesState = pagerValues,
        ) { weekday ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.timetable[weekday] ?: emptyList<AvailableLesson>(), { it.lesson.id }) {
                    LessonCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { sheetState.open(it) },
                        enabled = it.isAvailable,
                        lesson = it.lesson,
                        showGroup = true
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun NotFoundState(
    modifier: Modifier = Modifier,
) {
    Text(modifier = modifier, text = "Not Found")
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

}
