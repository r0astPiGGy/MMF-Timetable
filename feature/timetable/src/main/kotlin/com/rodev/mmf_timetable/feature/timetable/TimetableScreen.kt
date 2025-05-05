package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rodev.mmf_timetable.core.model.data.Weekday

@Composable
@Preview
private fun TimetableScreenPreview() {
//    TimetableScreen(
//        state = TimetableUiState.Timetable(
//            todayWeekday = Weekday.FRIDAY,
//            timetable = emptyMap(),
//            weekdays = emptyList(),
//            currentStudyWeek = 1
//        )
//    )
}

@Composable
internal fun TimetableRoute(
    modifier: Modifier = Modifier,
    viewModel: TimetableViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TimetableScreen(
        modifier = modifier,
        state = state
    )
}

@Composable
internal fun TimetableScreen(
    modifier: Modifier = Modifier,
    state: TimetableUiState
) {
    Column(modifier = modifier) {
        when (state) {
            TimetableUiState.CourseNotSelected -> CourseNotSelectedState()
            is TimetableUiState.Error -> ErrorState(
                text = state.exception.stackTraceToString()
            )
            TimetableUiState.Loading -> LoadingState()
            is TimetableUiState.Timetable -> {
                if (state.timetable.isNotEmpty()) {
                    Timetable(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = state
                    )
                } else {
                    EmptyState()
                }
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
    ShimmerTimetable(
        modifier = modifier
            .fillMaxSize()
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = "The timetable is empty")
}

@Composable
private fun CourseNotSelectedState(modifier: Modifier = Modifier) {

}