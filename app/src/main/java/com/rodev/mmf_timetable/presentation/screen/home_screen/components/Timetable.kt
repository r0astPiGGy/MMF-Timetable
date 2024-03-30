package com.rodev.mmf_timetable.presentation.screen.home_screen.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.model.Weekday
import com.rodev.mmf_timetable.presentation.components.HorizontalPagerAdapter
import com.rodev.mmf_timetable.presentation.components.PagerValuesState
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.AvailableLesson
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.HomeScreenState
import com.rodev.mmf_timetable.presentation.theme.MMF_TimetableTheme
import com.rodev.mmf_timetable.utils.DateUtils
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch


@Composable
fun LoadingTimetable(
    modifier: Modifier = Modifier,
) {

}

private fun randomLesson(): AvailableLesson {
    return lessonOf(
        Lesson(
            Weekday.MONDAY,
            type = Lesson.Type.PRACTICE,
            classroom = "606",
            subject = "Matan",
            timeStartMinutes = 100,
            timeEndMinutes = 1000,
            teacher = "Bruh",
            remarks = null,
            week = null
        )
    )
}

private fun lessonOf(lesson: Lesson): AvailableLesson {
    return AvailableLesson(
        wrappedLesson = lesson,
        available = true
    )
}

@Composable
private fun LoadingTimetablePreview() {
    MMF_TimetableTheme {
        Surface {
            LoadingTimetable(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@Preview
private fun TimetablePreview() {
    MMF_TimetableTheme(darkTheme = true) {
        Surface {
            Timetable(
                modifier = Modifier.fillMaxSize(),
                state = HomeScreenState(
                    timetable = mapOf(
                        Weekday.MONDAY to listOf(
                            randomLesson(),
                            randomLesson(),
                            randomLesson(),
                            randomLesson(),
                            randomLesson(),
                            randomLesson(),
                            randomLesson()
                        ),
                        Weekday.SATURDAY to listOf(
                            randomLesson(),
                            randomLesson()
                        ),
                        Weekday.TUESDAY to listOf(
                            randomLesson(),
                            randomLesson()
                        ),
                        Weekday.THURSDAY to listOf(
                            randomLesson(),
                            randomLesson()
                        )
                    ),
                )
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Timetable(
    modifier: Modifier = Modifier,
    state: HomeScreenState
) {
    if (state.weekdays.isEmpty()) return

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
        state.timetable?.mapValues { it.value.toImmutableList() } ?: emptyMap()
    }

    Column(
        modifier = modifier
    ) {
        WeekdayTabRow(
            currentPage = pagerState.currentPage,
            onPageClick = { index ->
                coroutineScope.launch {
                    pagerState.scrollToPage(index)
                }
            },
            weekdays = state.weekdays
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
fun LessonsPage(
    modifier: Modifier = Modifier,
    lessons: ImmutableList<AvailableLesson>
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
        items(lessons, { it.id }) {
            LessonCard(
                modifier = Modifier.fillMaxWidth(),
                lesson = it.wrappedLesson,
//                        onClick = {},
                enabled = it.available
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun WeekdayTabRow(
    modifier: Modifier = Modifier,
    currentPage: Int,
    onPageClick: (index: Int) -> Unit,
    weekdays: List<Weekday>
) {
    val tabWidths = remember(weekdays) {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(weekdays.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    ScrollableTabRow(
        modifier = modifier,
        indicator = { tabPositions ->
            CustomIndicator(
                modifier = Modifier.customTabIndicatorOffset(
                    currentTabPosition = tabPositions[currentPage],
                    tabWidth = tabWidths[currentPage]
                )
            )
        },
        tabs = {
            WeekdayTabList(
                currentPage = currentPage,
                weekdays = weekdays,
                onClick = onPageClick,
                onTextLayout = tabWidths::set
            )
        },
        selectedTabIndex = currentPage,
        edgePadding = 0.dp
    )
}

@Composable
fun WeekdayTabList(
    currentPage: Int,
    weekdays: List<Weekday>,
    onClick: (index: Int) -> Unit,
    onTextLayout: (index: Int, dp: Dp) -> Unit
) {
    val density = LocalDensity.current

    weekdays.forEachIndexed { index, weekday ->
        Tab(
//      unselectedContentColor = AppColors.DisabledDarkBlue,
            selected = currentPage == index,
            onClick = { onClick(index) },
            text = {
                Text(
                    text = stringResource(id = weekday.resId),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    onTextLayout = { result ->
                        onTextLayout(index, with(density) { result.size.width.toDp() })
                    }
                )
            }
        )
    }
}


@Composable
private fun CustomIndicator(
    modifier: Modifier = Modifier,
    height: Dp = 5.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val cornerRadius = CornerRadius(10f, 10f)
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(size = size, offset = Offset.Zero),
                    topLeft = cornerRadius,
                    topRight = cornerRadius,
                )
            )
        }
        drawPath(path, color = color)
    }
}

private fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "indicator width"
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "indicator offset"
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}