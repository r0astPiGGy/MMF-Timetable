package com.rodev.mmf_timetable.core.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier

@Immutable
data class PagerValuesState<T>(
    val values: List<T>
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> HorizontalPagerAdapter(
    modifier: Modifier = Modifier,
    valuesState: PagerValuesState<T>,
    state: PagerState = rememberPagerState { valuesState.values.size },
    content: @Composable PagerScope.(T) -> Unit
) {
    HorizontalPager(
        modifier = modifier,
        beyondViewportPageCount = 2,
        state = state
    ) {
        content(valuesState.values[it])
    }
}