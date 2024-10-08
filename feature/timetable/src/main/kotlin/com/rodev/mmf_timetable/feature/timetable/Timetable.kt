package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodev.mmf_timetable.R
import com.rodev.mmf_timetable.feature.timetable.components.HorizontalPagerAdapter
import com.rodev.mmf_timetable.feature.timetable.components.PagerValuesState
import com.rodev.mmf_timetable.feature.timetable.components.ShimmerWrapper
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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
        state.timetable.mapValues { it.value.toImmutableList() }
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
    lessons: ImmutableList<com.rodev.mmf_timetable.feature.timetable.LessonUiState>
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
//                onClick = {},
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
    weekdays: List<com.rodev.mmf_timetable.core.model.data.Weekday>
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
    weekdays: List<com.rodev.mmf_timetable.core.model.data.Weekday>,
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
                    text = stringResource(id = weekday.stringResource()),
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

private fun com.rodev.mmf_timetable.core.model.data.Weekday.stringResource(): Int {
    return when (this) {
        com.rodev.mmf_timetable.core.model.data.Weekday.SUNDAY -> R.string.weekday_sunday
        com.rodev.mmf_timetable.core.model.data.Weekday.MONDAY -> R.string.weekday_monday
        com.rodev.mmf_timetable.core.model.data.Weekday.TUESDAY -> R.string.weekday_tuesday
        com.rodev.mmf_timetable.core.model.data.Weekday.WEDNESDAY -> R.string.weekday_wednesday
        com.rodev.mmf_timetable.core.model.data.Weekday.THURSDAY -> R.string.weekday_thursday
        com.rodev.mmf_timetable.core.model.data.Weekday.FRIDAY -> R.string.weekday_friday
        com.rodev.mmf_timetable.core.model.data.Weekday.SATURDAY -> R.string.weekday_saturday
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