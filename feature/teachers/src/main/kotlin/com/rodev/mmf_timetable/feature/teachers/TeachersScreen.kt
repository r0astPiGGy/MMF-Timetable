package com.rodev.mmf_timetable.feature.teachers

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.rodev.mmf_timetable.core.model.data.Teacher
import com.rodev.mmf_timetable.core.ui.DynamicScaffoldPortal

@Composable
internal fun TeachersRoute(
    onGotoTeacher: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeachersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    TeachersScreen(
        modifier = modifier,
        onGotoTeacher = onGotoTeacher,
        state = state,
        onQueryChange = viewModel::onQueryChange,
        query = query
    )
}

@Composable
internal fun TeachersScreen(
    onGotoTeacher: (Long) -> Unit,
    state: TeachersUiState,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    DynamicScaffoldPortal(title = stringResource(R.string.teachers_title))

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = query,
            onValueChange = onQueryChange,
            label = { Text(stringResource(R.string.teachers_search_label)) },
            placeholder = { Text(stringResource(R.string.teachers_search_placeholder)) },
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        )
        Spacer(Modifier.size(8.dp))
        when (state) {
            is TeachersUiState.Error -> ErrorState(
                text = state.exception.stackTraceToString()
            )

            TeachersUiState.Loading -> LoadingState()
            is TeachersUiState.Teachers -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(state.teachers) { teacher ->
                        TeacherRow(
                            teacher = teacher,
                            modifier = Modifier.fillMaxWidth(),
                            onGotoTeacher = onGotoTeacher
                        )
                    }
                }
            }

            is TeachersUiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.teachers_empty))
                }
            }

            is TeachersUiState.NothingFoundByQuery -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.teachers_nothing_found_by_query))
                }
            }
        }
    }
}

@Composable
private fun TeacherRow(
    teacher: Teacher,
    onGotoTeacher: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = { onGotoTeacher(teacher.id) })
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
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
    }
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
