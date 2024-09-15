package com.rodev.mmf_timetable.ui

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
import com.rodev.mmf_timetable.utils.displayableTimeEnd
import com.rodev.mmf_timetable.utils.displayableTimeStart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonCard(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    lesson: Lesson
) {
    OutlinedCard(
        modifier = modifier,
        onClick = {},
        enabled = enabled,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
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
                    text = lesson.displayableTimeStart,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Text(
                    text = lesson.displayableTimeEnd,
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
                    text = lesson.teacher,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${lesson.classroom}${lesson.type?.let { " " + it.translated() } ?: ""} ${lesson.remarksOrEmpty()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun Lesson.remarksOrEmpty(): String {
    return remarks ?: ""
}

private fun Lesson.Type.translated(): String {
    return when (this) {
        Lesson.Type.LECTURE -> "л"
        Lesson.Type.PRACTICE -> "п"
    }
}