package com.rodev.mmf_timetable.feature.timetable.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun shimmerBrush(
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.5f),
        Color.LightGray.copy(alpha = 1.0f),
        Color.LightGray.copy(alpha = 0.5f),
        Color.LightGray.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )
}

@Composable
fun ShimmerWrapper(
    showShimmer: Boolean,
    wrappee: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (showShimmer) {
        Box {
            Column(
                modifier = Modifier.alpha(0.0f)
            ) {
                wrappee()
            }
            ShimmerCanvas(modifier = Modifier.matchParentSize())
        }
    } else {
        content()
    }
}

@Composable
fun StaticShimmerWrapper(
    showShimmer: Boolean,
    content: @Composable () -> Unit
) {
    ShimmerWrapper(
        showShimmer = showShimmer,
        wrappee = content,
        content = content
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShimmerCarousel(
    modifier: Modifier = Modifier,
    showShimmer: Boolean,
    shimmerPageItems: Int = 4,
    pageSize: PageSize,
    wrappee: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (showShimmer) {
        HorizontalPager(
            modifier = modifier,
            state = rememberPagerState { shimmerPageItems },
            pageSize = pageSize,
            beyondViewportPageCount = 1,
            userScrollEnabled = false,
            pageSpacing = 22.dp,
        ) { index ->
            StaticShimmerWrapper(true, content = wrappee)
        }
    } else {
        content()
    }
}

@Composable
fun ShimmerColumn(
    modifier: Modifier = Modifier,
    showShimmer: Boolean,
    amount: Int,
    itemWrapee: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    if (showShimmer) {
        Column(
            modifier = modifier.verticalScroll(state = rememberScrollState(), enabled = false),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(amount) {
                StaticShimmerWrapper(showShimmer = true, content = itemWrapee)
            }
        }
    } else {
        content()
    }
}

@Composable
fun Shimmer(
    modifier: Modifier = Modifier,
    showShimmer: Boolean,
    content: @Composable () -> Unit,
) {
    if (showShimmer) {
        ShimmerCanvas(modifier = modifier)
    } else {
        content()
    }
}

@Composable
fun IndeterminateShimmer(
    modifier: Modifier = Modifier
) {
    ShimmerCanvas(modifier)
}

@Composable
private fun ShimmerCanvas(
    modifier: Modifier
) {
    val shimmerBrush = shimmerBrush()
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp)),
    ) {
        drawRect(shimmerBrush)
    }
}