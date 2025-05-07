package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.core.model.data.Weekday.*
import com.rodev.mmf_timetable.core.ui.DynamicScaffoldPortal
import com.rodev.mmf_timetable.core.ui.TabRow
import com.rodev.mmf_timetable.core.ui.HorizontalPagerAdapter
import com.rodev.mmf_timetable.core.ui.LessonCard
import com.rodev.mmf_timetable.core.ui.PagerValuesState
import com.rodev.mmf_timetable.feature.timetable.components.ShimmerWrapper
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Timetable(
    modifier: Modifier = Modifier,
    state: TimetableUiState.Timetable
) {
    val pagerState = rememberPagerState { state.weekdays.size }

    LaunchedEffect(state.weekdays) {
        val index = state.weekdays.indexOf(state.todayWeekday)

        if (index >= 0) {
            pagerState.scrollToPage(index)
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val pagerValues = remember { PagerValuesState(state.weekdays) }
    val mappedLessons = remember(state) {
        state.timetable.mapValues { it.value }
    }


    Column(
        modifier = modifier
    ) {
        TabRow(
            currentPage = pagerState.currentPage,
            onPageClick = { index ->
                coroutineScope.launch {
                    pagerState.scrollToPage(index)
                }
            },
            list = state.weekdays,
            textProvider = { weekdayToString(it) }
        )

        HorizontalPagerAdapter(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            valuesState = pagerValues,
        ) { weekday ->
            LessonsPage(
                lessons = mappedLessons[weekday]!!
            )
        }
    }
}

@Composable
private fun weekdayToString(weekday: Weekday): String {
    return stringResource(weekday.stringResource())
}

private fun Weekday.stringResource(): Int {
    return when (this) {
        SUNDAY -> R.string.weekday_sunday
        MONDAY -> R.string.weekday_monday
        TUESDAY -> R.string.weekday_tuesday
        WEDNESDAY -> R.string.weekday_wednesday
        THURSDAY -> R.string.weekday_thursday
        FRIDAY -> R.string.weekday_friday
        SATURDAY -> R.string.weekday_saturday
    }
}

@Composable
fun LessonsPage(
    modifier: Modifier = Modifier,
    lessons: List<AvailableLesson>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(lessons, { it.lesson.id }) {
            LessonCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
                enabled = it.isAvailable,
                lesson = it.lesson
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
