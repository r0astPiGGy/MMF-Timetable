package com.rodev.mmf_timetable.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        modifier = modifier,
        state = state
    )
}

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsUiState
) {
    Column(modifier = modifier) {
        when (state) {
            is SettingsUiState.Error -> ErrorState(
                text = state.exception.stackTraceToString()
            )
            SettingsUiState.Loading -> LoadingState()
            is SettingsUiState.Settings -> {

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
