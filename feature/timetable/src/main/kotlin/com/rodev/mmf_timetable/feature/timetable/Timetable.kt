package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.LessonTeacher
import com.rodev.mmf_timetable.core.model.data.Teacher
import com.rodev.mmf_timetable.core.ui.DatePickerModal
import com.rodev.mmf_timetable.core.ui.HorizontalPagerAdapter
import com.rodev.mmf_timetable.core.ui.ItemDetailsBottomSheet
import com.rodev.mmf_timetable.core.ui.LessonCard
import com.rodev.mmf_timetable.core.ui.PagerValuesState
import com.rodev.mmf_timetable.core.ui.SelectedMonthRow
import com.rodev.mmf_timetable.core.ui.ShimmerWrapper
import com.rodev.mmf_timetable.core.ui.TeacherRow
import com.rodev.mmf_timetable.core.ui.WeekDaySelectRow
import com.rodev.mmf_timetable.core.ui.rememberItemDetailsBottomSheetState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
fun ShimmerTimetable(
    modifier: Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState(), enabled = false),
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize(),
            userScrollEnabled = false
        ) {
            items(5) {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ShimmerWrapper(showShimmer = true, wrappee = {
                        Text("Wednesday", fontSize = 16.sp)
                    }) {}
                }
            }
        }

        Spacer(Modifier.size(8.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(6) {
                ShimmerWrapper(showShimmer = true, wrappee = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {}
                }) {}
            }
        }


    }
}



@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Timetable(
    modifier: Modifier = Modifier,
    state: TimetableUiState.Timetable,
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
                modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
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
                .fillMaxWidth(),
            state = pagerState,
            valuesState = pagerValues,
        ) { weekday ->
            LessonsPage(
                lessons = state.timetable[weekday] ?: emptyList(),
                onLessonClick = sheetState::open
            )
        }
    }
}


@Composable
fun LessonsPage(
    modifier: Modifier = Modifier,
    lessons: List<AvailableLesson>,
    onLessonClick: (AvailableLesson) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lessons, { it.lesson.id }) {
            LessonCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onLessonClick(it) },
                enabled = it.isAvailable,
                lesson = it.lesson
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
