package com.rodev.mmf_timetable.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale as LocalLocale
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.core.model.data.Weekday.FRIDAY
import com.rodev.mmf_timetable.core.model.data.Weekday.MONDAY
import com.rodev.mmf_timetable.core.model.data.Weekday.SATURDAY
import com.rodev.mmf_timetable.core.model.data.Weekday.SUNDAY
import com.rodev.mmf_timetable.core.model.data.Weekday.THURSDAY
import com.rodev.mmf_timetable.core.model.data.Weekday.TUESDAY
import com.rodev.mmf_timetable.core.model.data.Weekday.WEDNESDAY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.toString

@Immutable
data class DateWeekday(
    val date: LocalDate,
    val weekday: Weekday
)

@Immutable
data class Week(
    val days: List<DateWeekday>,
) {

    override fun hashCode(): Int {
        return days.fold(1) { acc, w -> 31 * acc + w.hashCode() }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Week

        return days.containsAll(other.days)
    }
}

fun weekOf(date: LocalDate): List<DateWeekday> {
    // 0 - 6
    val offset = date.dayOfWeek.value - 1
    return listOf(
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    ).mapIndexed { i, week ->
        return@mapIndexed DateWeekday(
            date = date.plus(i - offset, DateTimeUnit.DAY),
            weekday = week
        )
    }
}

private fun LocalDate.formatDayOfMonth(with: Locale): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM", with)
    return toJavaLocalDate().format(formatter)
}


@Composable
fun SelectedMonthRow(
    selectedDate: LocalDate,
    onDayPickerSelect: () -> Unit
) {
    val lang = LocalLocale.current.language
    val locale = remember(lang) { Locale(lang) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = selectedDate
                .formatDayOfMonth(with = locale),
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(
            onClick = onDayPickerSelect
        ) {
            Icon(Icons.Outlined.CalendarMonth, contentDescription = null)
        }
    }
}

@Stable
class TimetableState(
    selectedDate: LocalDate,
    private val coroutineScope: CoroutineScope,
    private val pagerState: PagerState
) {
    val selected: DateWeekday by derivedStateOf {
        TODO()
    }

    val week: List<DateWeekday> by derivedStateOf {
        TODO()
    }

    fun select(weekday: DateWeekday) {
        if (weekday != selected) {
            coroutineScope.launch {
                pagerState.scrollToPage(week.indexOf(weekday))
            }
        }
    }
}

@Composable
fun WeekDaySelectRow(
    week: List<DateWeekday>,
    selectedDate: DateWeekday,
    onTabSelect: (DateWeekday) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        week.forEach {
            val isSelected = selectedDate == it
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .then(
                        if (isSelected) Modifier.background(
                            color = MaterialTheme.colorScheme.primary,
                        ) else Modifier
                    )
                    .clickable(onClick = { onTabSelect(it) })
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
