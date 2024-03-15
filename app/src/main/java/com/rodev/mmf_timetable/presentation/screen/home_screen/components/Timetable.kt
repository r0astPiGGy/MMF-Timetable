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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import com.rodev.mmf_timetable.presentation.components.HorizontalPagerAdapter
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.HomeScreenState
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.provideWeekdays
import com.rodev.mmf_timetable.utils.DateUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Timetable(
    modifier: Modifier = Modifier,
    state: HomeScreenState
) {
    val weekdays = remember(state) { state.provideWeekdays() }

    if (weekdays.isEmpty()) return

    val density = LocalDensity.current
    val tabWidths = remember(weekdays) {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(weekdays.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    val pagerState = rememberPagerState { weekdays.size }
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(weekdays) {
        val index = weekdays.indexOf(DateUtils.getCurrentWeekday())

        if (index >= 0) {
            pagerState.scrollToPage(index)
        }
    }

    Column(
        modifier = modifier
    ) {
        ScrollableTabRow(
            indicator = { tabPositions ->
                CustomIndicator(
                    modifier = Modifier.customTabIndicatorOffset(
                        currentTabPosition = tabPositions[selectedTabIndex],
                        tabWidth = tabWidths[selectedTabIndex]
                    )
                )
            },
            tabs = {
                weekdays.forEachIndexed { index, weekday ->
                    Tab(
//                        unselectedContentColor = AppColors.DisabledDarkBlue,
                        selected = selectedTabIndex == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = stringResource(id = weekday.resId),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                onTextLayout = { result ->
                                    tabWidths[index] =
                                        with(density) { result.size.width.toDp() }
                                }
                            )
                        }
                    )
                }
            },
            selectedTabIndex = selectedTabIndex,
            edgePadding = 0.dp
        )
        HorizontalPagerAdapter(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            pagerValues = weekdays,
        ) { weekday ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(state.timetable?.get(weekday)!!) {
                    LessonCard(
                        modifier = Modifier.fillMaxWidth(),
                        lesson = it.wrappedLesson,
                        onClick = {},
                        enabled = it.available
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