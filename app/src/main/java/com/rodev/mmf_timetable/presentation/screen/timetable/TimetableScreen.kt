package com.rodev.mmf_timetable.presentation.screen.timetable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rodev.mmf_timetable.R
import com.rodev.mmf_timetable.presentation.screen.timetable.components.ShimmerTimetable
import com.rodev.mmf_timetable.presentation.screen.timetable.components.Timetable
import com.rodev.mmf_timetable.presentation.screen.timetable.components.TimetableTopAppBar
import com.rodev.mmf_timetable.presentation.screen.timetable.state.TimetableUiState

@Composable
@Preview
private fun TimetableScreenPreview() {
    TimetableScreen(
        state = TimetableUiState.Success(),
        onMenuButtonClick = {}
    )
}

@Composable
fun TimetableScreen(
    modifier: Modifier = Modifier,
    state: TimetableUiState,
    onMenuButtonClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TimetableTopAppBar(
                title = stringResource(R.string.timetable),
                subTitle = state
                    .let { it as? TimetableUiState.Success }
                    ?.currentStudyWeek
                    ?.let { stringResource(R.string.current_week, it) },
                onMenuButtonClick = onMenuButtonClick
            )
        }
    ) { paddings ->
        when (state) {
            TimetableUiState.CourseNotSelected -> {

            }
            is TimetableUiState.Error -> {
                Text(
                    modifier = Modifier
                        .padding(paddings), text = state.exception.stackTraceToString())
            }
            TimetableUiState.Loading -> {
                ShimmerTimetable(
                    modifier = Modifier
                        .padding(paddings)
                        .fillMaxSize()
                )
            }
            is TimetableUiState.Success -> {
                if (state.timetable.isNotEmpty()) {
                    Timetable(
                        modifier = Modifier
                            .padding(paddings)
                            .fillMaxSize(),
                        state = state
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .padding(paddings), text = "The timetable is empty")
                }
            }
        }
    }
}