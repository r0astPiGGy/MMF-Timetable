package com.rodev.mmf_timetable.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodev.mmf_timetable.core.model.data.Availability
import com.rodev.mmf_timetable.core.model.data.Lesson
import kotlinx.datetime.DateTimePeriod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OngoingLessonCard(
    modifier: Modifier = Modifier,
    lesson: Lesson,
    progress: Float,
    remaining: DateTimePeriod,
    onClick: () -> Unit = {},
    subject: @Composable ColumnScope.() -> Unit = {
        Text(
            text = lesson.subject,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp
        )
    },
    classroom: @Composable ColumnScope.() -> Unit = {
        Text(
            text = lesson.classroom?.name.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
    },
    teachers: @Composable ColumnScope.() -> Unit = {
        if (lesson.teachers.size > 1) {
            Text(
                text = lesson.teachers.joinToString(", ") { it.name },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    },
    courseGroup: @Composable ColumnScope.() -> Unit = {
        val info1 = listOfNotNull<String>(
            stringResource(
                R.string.ui_course_group,
                lesson.group?.course ?: 0,
                lesson.group?.name.toString()
            ),
            lesson.subGroup?.name
        )
        if (info1.isNotEmpty()) {
            Text(
                text = info1.joinToString(" ") { it }
            )
        }
    },
    availability: @Composable ColumnScope.() -> Unit = {
        val info = listOfNotNull<String>(
            lesson.type?.takeIf { it.isNotEmpty() },
            "1н".takeIf {
                lesson.availability.contains(
                    Availability.OddWeek
                )
            },
            "2н".takeIf { lesson.availability.contains(Availability.EvenWeek) },
        ).joinToString(" ") { it }
        Text(
            text = info,
            style = MaterialTheme.typography.bodyMedium
        )
    },
    additionalInfo: @Composable ColumnScope.() -> Unit = {
        if (lesson.additionalInfo.isNotEmpty()) {
            Text(
                text = lesson.additionalInfo.joinToString(" ") { it },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
) {
    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Text(
                        text = "Осталось " + listOfNotNull<String>(
                            remaining.hours.takeIf { it != 0 }?.let { "$it ч." },
                            remaining.minutes.takeIf { it != 0 }?.let { "$it мин." },
                        ).let {
                            if (it.isEmpty()) listOf("меньше минуты") else it
                        }.joinToString(" ") { it.toString() },
                        style = MaterialTheme.typography.labelMedium
                            .merge(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    subject()
                    classroom()
                    teachers()
                    courseGroup()
                    availability()
                    additionalInfo()
                }
            }
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                progress = { progress },
                gapSize = 0.dp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextLessonCard(
    modifier: Modifier = Modifier,
    lesson: Lesson,
    remaining: DateTimePeriod,
    onClick: () -> Unit = {},
    subject: @Composable ColumnScope.() -> Unit = {
        Text(
            text = lesson.subject,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp
        )
    },
    classroom: @Composable ColumnScope.() -> Unit = {
        Text(
            text = lesson.classroom?.name.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
    },
    teachers: @Composable ColumnScope.() -> Unit = {
        if (lesson.teachers.isNotEmpty()) {
            Text(
                text = lesson.teachers.joinToString(", ") { it.name },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    },
    courseGroup: @Composable ColumnScope.() -> Unit = {
        val info1 = listOfNotNull<String>(
            stringResource(
                R.string.ui_course_group,
                lesson.group?.course ?: 0,
                lesson.group?.name.toString()
            ),
            lesson.subGroup?.name
        )
        if (info1.isNotEmpty()) {
            Text(
                text = info1.joinToString(" ") { it }
            )
        }
    },
    availability: @Composable ColumnScope.() -> Unit = {
        val info = listOfNotNull<String>(
            lesson.type?.takeIf { it.isNotEmpty() },
            "1н".takeIf {
                lesson.availability.contains(
                    Availability.OddWeek
                )
            },
            "2н".takeIf { lesson.availability.contains(Availability.EvenWeek) },
        ).joinToString(" ") { it }
        if (info.isNotEmpty()) {
            Text(
                text = info,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    },
    additionalInfo: @Composable ColumnScope.() -> Unit = {
        if (lesson.additionalInfo.isNotEmpty()) {
            Text(
                text = lesson.additionalInfo.joinToString(" ") { it },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
) {
    OutlinedCard(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = "До начала " + listOfNotNull<String>(
                        remaining.hours.takeIf { it != 0 }?.let { "$it ч." },
                        remaining.minutes.takeIf { it != 0 }?.let { "$it мин." },
                    ).let {
                        if (it.isEmpty()) listOf("меньше минуты") else it
                    }.joinToString(" ") { it.toString() },
                    style = MaterialTheme.typography.labelMedium
                        .merge(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                subject()
                classroom()
                teachers()
                courseGroup()
                availability()
                additionalInfo()
            }
        }
    }
}
