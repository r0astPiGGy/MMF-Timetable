package com.rodev.mmf_timetable.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodev.mmf_timetable.core.model.data.Lesson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    lesson: Lesson
) {
    OutlinedCard(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row (
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.requiredWidth(60.dp)
            ) {
                Text(
                    text = lesson.timeStart,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Text(
                    text = lesson.timeEnd,
                    fontSize = 16.sp
                )
            }

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
            )

            Column(
                modifier = Modifier
            ) {
                Text(
                    text = lesson.subject,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.sp
                )
                Text(
                    text = lesson.teachers.joinToString(",") { it.name },
                    style = MaterialTheme.typography.bodyMedium
                )
                val info = listOf<String?>(lesson.classroom?.name, lesson.type).mapNotNull { it }.joinToString(" ") { it }
                Text(
                    text = info,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (lesson.additionalInfo.isNotEmpty()) {
                    Text(
                        text = lesson.additionalInfo.joinToString(" ") { it },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}