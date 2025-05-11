package com.rodev.mmf_timetable.feature.classroom

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rodev.mmf_timetable.core.ui.DynamicScaffoldPortal

@Composable
internal fun ClassroomRoute(
    modifier: Modifier = Modifier,
    viewModel: ClassroomViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ClassroomScreen(
        modifier = modifier,
        state = state
    )
}

@Composable
internal fun ClassroomScreen(
    state: ClassroomUiState,
    modifier: Modifier = Modifier,
) {
    DynamicScaffoldPortal(title = stringResource(R.string.classroom_title))

    Column(modifier = modifier) {
        when (state) {
            is ClassroomUiState.Error -> ErrorState(
                text = state.exception.stackTraceToString()
            )
            ClassroomUiState.Loading -> LoadingState()
            is ClassroomUiState.ClassroomDetails -> {

            }

            ClassroomUiState.NotFound -> NotFoundState()
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
