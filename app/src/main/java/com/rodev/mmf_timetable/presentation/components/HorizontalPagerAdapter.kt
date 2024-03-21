package com.rodev.mmf_timetable.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> HorizontalPagerAdapter(
    modifier: Modifier = Modifier,
    pagerValues: List<T>,
    onPageChange: ((T) -> Unit)? = null,
    state: PagerState = rememberPagerState { pagerValues.size },
    content: @Composable PagerScope.(T) -> Unit
) {
//    if (onPageChange != null) {
//        LaunchedEffect(state.currentPage) {
//            onPageChange(pagerValues[state.currentPage])
//        }
//    }

    HorizontalPager(
        modifier = modifier,
        beyondBoundsPageCount = 1,
        state = state
    ) {
        content(pagerValues[it])
    }
}