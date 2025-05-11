package com.rodev.mmf_timetable.feature.classrooms

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rodev.mmf_timetable.core.ui.DynamicScaffoldPortal
//import com.rodev.mmf_timetable.feature.classrooms.R

@Composable
internal fun ClassroomsRoute(
    onGotoClassroom: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassroomsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ClassroomsScreen(
        modifier = modifier,
        onGotoClassroom = onGotoClassroom,
        state = state
    )
}

@Composable
internal fun ClassroomsScreen(
    onGotoClassroom: (Long) -> Unit,
    state: ClassroomsUiState,
    modifier: Modifier = Modifier,
) {
//    DynamicScaffoldPortal(title = stringResource(com.rodev.mmf_timetable.feature.classrooms.R))

    Column(modifier = modifier) {
        when (state) {
            is ClassroomsUiState.Error -> ErrorState(
                text = state.exception.stackTraceToString()
            )
            ClassroomsUiState.Loading -> LoadingState()
            is ClassroomsUiState.Classrooms -> {

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
