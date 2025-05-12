package com.rodev.mmf_timetable.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rodev.mmf_timetable.core.model.data.LessonTeacher

@Composable
fun TeacherRow(
    teacher: LessonTeacher,
    modifier: Modifier = Modifier,
    onGotoTeacher: (Long) -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = { onGotoTeacher(teacher.id) })
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
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
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier.basicMarquee(),
                maxLines = 1,
                text = teacher.fullName ?: teacher.name,
                style = MaterialTheme.typography.bodyLarge
            )
            if (teacher.position != null) {
                Text(
                    modifier = Modifier.basicMarquee(),
                    maxLines = 1,
                    text = teacher.position.toString(),
                    style = MaterialTheme.typography.bodyMedium
                        .merge(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }
        Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = null)
    }
}

