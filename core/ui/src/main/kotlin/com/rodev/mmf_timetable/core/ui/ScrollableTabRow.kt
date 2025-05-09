package com.rodev.mmf_timetable.core.ui

import android.R.attr.fontWeight
import android.R.attr.onClick
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> TabRow(
    modifier: Modifier = Modifier,
    currentPage: Int,
    onPageClick: (index: Int) -> Unit,
    list: List<T>,
    textProvider: @Composable (T) -> String,
) {
    val tabWidths = remember(list) {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(list.size) {
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
            TabList(
                currentPage = currentPage,
                tabs = list,
                onClick = onPageClick,
                onTextLayout = tabWidths::set,
                textProvider = textProvider
            )
        },
        divider = { },
        selectedTabIndex = currentPage,
        edgePadding = 0.dp
    )
}

@Composable
fun <T> TabList(
    currentPage: Int,
    tabs: List<T>,
    onClick: (index: Int) -> Unit,
    onTextLayout: (index: Int, dp: Dp) -> Unit,
    textProvider: @Composable (T) -> String
) {
    val density = LocalDensity.current

    tabs.forEachIndexed { index, element ->
        Tab(
            selected = currentPage == index,
            onClick = { onClick(index) },
            text = {
                Text(
                    text = textProvider(element),
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
