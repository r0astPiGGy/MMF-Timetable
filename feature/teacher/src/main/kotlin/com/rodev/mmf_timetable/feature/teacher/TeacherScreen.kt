package com.rodev.mmf_timetable.feature.teacher

import android.content.ClipData
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.rodev.mmf_timetable.core.model.data.Teacher
import com.rodev.mmf_timetable.core.ui.DynamicScaffoldPortal
import com.rodev.mmf_timetable.core.ui.LessonCard
import com.rodev.mmf_timetable.core.ui.TeacherOngoingLessonCard
import com.rodev.mmf_timetable.feature.teacher.model.CurrentLesson
import kotlinx.coroutines.launch

@Composable
internal fun TeacherRoute(
    modifier: Modifier = Modifier,
    viewModel: TeacherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TeacherScreen(
        modifier = modifier,
        state = state
    )
}

@Composable
internal fun TeacherScreen(
    state: TeacherUiState,
    modifier: Modifier = Modifier,
) {
    DynamicScaffoldPortal(title = stringResource(R.string.teacher_title))

    Column(modifier = modifier) {
        when (state) {
            is TeacherUiState.Error -> ErrorState(
                text = state.exception.stackTraceToString()
            )
            TeacherUiState.Loading -> LoadingState()
            is TeacherUiState.TeacherDetails -> {
                TeacherDetails(
                    teacher = state.teacher,
                    currentLesson = state.currentLesson,
                    modifier = modifier
                )
            }

            TeacherUiState.NotFound -> NotFoundState()
        }
    }
}

@Composable
private fun TeacherDetails(
    teacher: Teacher,
    currentLesson: CurrentLesson,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item(contentType = "header") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = teacher.imageUrl,
                        contentDescription = "Teacher image",
                        contentScale = ContentScale.Crop,
                    )
                }
                val clipboard = LocalClipboard.current
                val coroutineScope = rememberCoroutineScope()

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = teacher.fullName ?: teacher.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .basicMarquee()
                    )
                    val email = teacher.email
                    if (!email.isNullOrEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {
                                    coroutineScope.launch {
                                        val data = ClipData.newPlainText("email", email)
                                        val clipEntry = ClipEntry(data)
                                        clipboard.setClipEntry(clipEntry)
                                    }
                                })
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Email, contentDescription = "email")
                            Spacer(Modifier.size(4.dp))
                            Text(text = email,
                                modifier = Modifier
                                    .weight(1f)
                                    .basicMarquee()
                                )
                            Icon(Icons.Default.ContentCopy, contentDescription = "copy", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    val phone = teacher.phone
                    if (!phone.isNullOrEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {
                                    coroutineScope.launch {
                                        val data = ClipData.newPlainText("phone", phone)
                                        val clipEntry = ClipEntry(data)
                                        clipboard.setClipEntry(clipEntry)
                                    }
                                })
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "email")
                            Spacer(Modifier.size(4.dp))
                            Text(text = phone,
                                modifier = Modifier
                                    .weight(1f)
                                    .basicMarquee()
                            )
                            Icon(Icons.Default.ContentCopy, contentDescription = "copy", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
        item(contentType = "currentLesson") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                when (currentLesson) {
                    is CurrentLesson.Current -> {
                        Text(
                            text = "Текущая пара",
                            style = MaterialTheme.typography.labelLarge
                        )
                        TeacherOngoingLessonCard(
                            lesson = currentLesson.lesson,
                            remaining = currentLesson.remaining,
                            progress = currentLesson.progress
                        )
                    }
                    is CurrentLesson.Next -> {
                        Text("Следующая пара, до начала ${currentLesson.remaining.hours} ч. ${currentLesson.remaining.minutes} мин.")
                        LessonCard(
                            lesson = currentLesson.lesson,
                            onClick = {}
                        )
                    }
                    CurrentLesson.NoLessonToday -> {

                    }
                }
            }
        }
    }
}

@Composable
private fun NotFoundState(
    modifier: Modifier = Modifier,
) {
    Text(modifier = modifier, text = "Not Found")
}

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(modifier = modifier, text = text)
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {

}
