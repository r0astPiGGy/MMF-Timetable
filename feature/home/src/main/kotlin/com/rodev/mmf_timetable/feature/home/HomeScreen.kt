package com.rodev.mmf_timetable.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        state = state
    )
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState
) {
    Column(modifier = modifier) {
        when (state) { // Current lesson / group
            is HomeUiState.Error -> ErrorState(
                text = state.exception.stackTraceToString()
            )
            HomeUiState.Loading -> LoadingState()
            is HomeUiState.Home -> {

            }

            HomeUiState.CourseNotSelected -> {

            }
        }
        Row {
            Card {
                // Teachers
            }
            Card {
                // Rooms
            }
        }
        Card {
            // Add widget
        }
    }
}

@Composable
private fun CourseNotSelectedState(modifier: Modifier = Modifier) {

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
