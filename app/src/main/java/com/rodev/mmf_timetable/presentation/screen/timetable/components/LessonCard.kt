package com.rodev.mmf_timetable.presentation.screen.timetable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodev.mmf_timetable.presentation.screen.timetable.state.LessonUiState
import com.rodev.mmf_timetable.core.designsystem.theme.MMF_TimetableTheme
import com.rodev.mmf_timetable.utils.displayableTimeEnd
import com.rodev.mmf_timetable.utils.displayableTimeStart
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonCard(
    modifier: Modifier = Modifier,
//    onClick: () -> Unit,
    enabled: Boolean = true,
    lesson: com.rodev.mmf_timetable.core.model.data.Lesson
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


@Composable
@Preview
fun LessonCardPreview() {
    com.rodev.mmf_timetable.core.designsystem.theme.MMF_TimetableTheme(darkTheme = false) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(5) {
                    LessonCard(
                        modifier = Modifier.fillMaxWidth(),
//                        onClick = {},
                        lesson = dummyLessonRandomTime().wrappedLesson,
                        enabled = true
                    )
                }
            }
        }
    }
}

private fun dummyLessonRandomTime(): LessonUiState {
    val start = Random.nextInt(200, 1320)
    val duration = 90

    return LessonUiState(
        wrappedLesson = com.rodev.mmf_timetable.core.model.data.Lesson(
            com.rodev.mmf_timetable.core.model.data.Weekday.MONDAY,
            com.rodev.mmf_timetable.core.model.data.Lesson.Type.LECTURE,
            classroom = "606",
            subject = "Математический анализ",
            timeStartMinutes = start,
            timeEndMinutes = start + duration,
            teacher = "Васильев И.Л",
            remarks = "2н",
            weekType = com.rodev.mmf_timetable.core.model.data.Lesson.WeekType.EVEN
        ),
        available = true
    )
}

private fun dummyLesson(): LessonUiState {
    return LessonUiState(
        wrappedLesson = com.rodev.mmf_timetable.core.model.data.Lesson(
            com.rodev.mmf_timetable.core.model.data.Weekday.MONDAY,
            com.rodev.mmf_timetable.core.model.data.Lesson.Type.LECTURE,
            classroom = "606",
            subject = "Математический анализ",
            timeStartMinutes = 615,
            timeEndMinutes = 665,
            teacher = "Васильев И.Л",
            remarks = "2н",
            weekType = com.rodev.mmf_timetable.core.model.data.Lesson.WeekType.EVEN
        ),
        available = true
    )
}

private fun com.rodev.mmf_timetable.core.model.data.Lesson.remarksOrEmpty(): String {
    return remarks ?: ""
}

private fun com.rodev.mmf_timetable.core.model.data.Lesson.Type.translated(): String {
    return when (this) {
        com.rodev.mmf_timetable.core.model.data.Lesson.Type.LECTURE -> "л"
        com.rodev.mmf_timetable.core.model.data.Lesson.Type.PRACTICE -> "п"
    }
}