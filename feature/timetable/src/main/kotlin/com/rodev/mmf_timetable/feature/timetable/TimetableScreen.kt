package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
private fun TimetableScreenPreview() {
    TimetableScreen(
        timetableState = TimetableUiState.Timetable()
    )
}

@Composable
internal fun TimetableRoute(
    modifier: Modifier = Modifier,
    viewModel: TimetableViewModel
) {

}

@Composable
internal fun TimetableScreen(
    modifier: Modifier = Modifier,
    timetableState: TimetableUiState
) {
    Column(modifier = modifier) {
        when (timetableState) {
            TimetableUiState.CourseNotSelected -> {

            }
            is TimetableUiState.Error -> {
                Text(text = timetableState.exception.stackTraceToString())
            }
            TimetableUiState.Loading -> {
                ShimmerTimetable(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            is TimetableUiState.Timetable -> {
                if (timetableState.timetable.isNotEmpty()) {
                    Timetable(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = timetableState
                    )
                } else {
                    Text(text = "The timetable is empty")
                }
            }
        }
    }
}