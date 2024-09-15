package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rodev.mmf_timetable.presentation.screen.timetable.components.ShimmerTimetable
import com.rodev.mmf_timetable.presentation.screen.timetable.components.Timetable
import com.rodev.mmf_timetable.presentation.screen.timetable.components.TimetableTopAppBar

@Composable
@Preview
private fun TimetableScreenPreview() {
    TimetableScreen(
        timetableState = TimetableUiState.Timetable(),
        onMenuButtonClick = {}
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
    timetableState: TimetableUiState,
    onMenuButtonClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TimetableTopAppBar(
                title = stringResource(R.string.timetable),
                subTitle = timetableState
                    .let { it as? TimetableUiState.Timetable }
                    ?.currentStudyWeek
                    ?.let { stringResource(R.string.current_week, it) },
                onMenuButtonClick = onMenuButtonClick
            )
        }
    ) { paddings ->
        when (timetableState) {
            TimetableUiState.CourseNotSelected -> {

            }
            is TimetableUiState.Error -> {
                Text(
                    modifier = Modifier
                        .padding(paddings), text = timetableState.exception.stackTraceToString())
            }
            TimetableUiState.Loading -> {
                ShimmerTimetable(
                    modifier = Modifier
                        .padding(paddings)
                        .fillMaxSize()
                )
            }
            is TimetableUiState.Timetable -> {
                if (timetableState.timetable.isNotEmpty()) {
                    Timetable(
                        modifier = Modifier
                            .padding(paddings)
                            .fillMaxSize(),
                        state = timetableState
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