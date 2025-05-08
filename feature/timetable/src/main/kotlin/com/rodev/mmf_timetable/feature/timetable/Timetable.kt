package com.rodev.mmf_timetable.feature.timetable

import android.R.attr.text
import android.util.Log
import android.util.Log.i
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion.then
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.stylusHoverIcon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale as LocalLocale
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.rodev.mmf_timetable.core.ui.DatePickerModal
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

private fun LocalDate.formatDayOfMonth(with: Locale): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM", with)
    return toJavaLocalDate().format(formatter)
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Timetable(
    modifier: Modifier = Modifier,
    state: TimetableUiState.Timetable,
    onDateSelect: (LocalDate) -> Unit
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

    val lang = LocalLocale.current.language
    val locale = remember(lang) { Locale(lang) }

    val coroutineScope = rememberCoroutineScope()
    val pagerValues = remember(state.week) { PagerValuesState(state.week) }

    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { it?.let(onDateSelect) },
            onDismiss = { showDatePicker = false }
        )
    }
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(start = 16.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = state.selectedDate.date
                    .formatDayOfMonth(with = locale),
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(
                onClick = { showDatePicker = true }
            ) {
                Icon(Icons.Outlined.CalendarMonth, contentDescription = null)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            state.week.forEachIndexed { i, it ->
                val isSelected = state.selectedDate == it
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .then(
                            if (isSelected) Modifier.background(
                                color = MaterialTheme.colorScheme.primary,
                            ) else Modifier
                        )
                        .clickable(onClick = {
                            if (!isSelected) {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(i)
                                }
                            }
                        })
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                        .weight(1f),
                ) {
                    Text(
                        text = weekdayToString(it.weekday),
                        style = MaterialTheme.typography.labelSmall
                            .merge(color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                    Text(
                        text = it.date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.labelLarge.let {
                            if (isSelected) it.merge(color = MaterialTheme.colorScheme.onPrimary) else it
                        }
                    )
                }
            }
        }

        HorizontalPagerAdapter(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = pagerState,
            valuesState = pagerValues,
        ) { weekday ->
            LessonsPage(
                lessons = state.timetable[weekday] ?: emptyList()
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
        SUNDAY -> R.string.weekday_sunday_short
        MONDAY -> R.string.weekday_monday_short
        TUESDAY -> R.string.weekday_tuesday_short
        WEDNESDAY -> R.string.weekday_wednesday_short
        THURSDAY -> R.string.weekday_thursday_short
        FRIDAY -> R.string.weekday_friday_short
        SATURDAY -> R.string.weekday_saturday_short
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
