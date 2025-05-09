package com.rodev.mmf_timetable.feature.timetable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.core.ui.DynamicScaffoldPortal
import kotlinx.datetime.LocalDate

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
    viewModel: TimetableViewModel = hiltViewModel(),
    onGotoRoom: (Long) -> Unit,
    onGotoTeacher: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TimetableScreen(
        modifier = modifier,
        state = state,
        onDateSelect = viewModel::selectDate,
        onGotoTeacher = onGotoTeacher,
        onGotoRoom = onGotoRoom
    )
}

@Composable
internal fun TimetableScreen(
    modifier: Modifier = Modifier,
    state: TimetableUiState,
    onDateSelect: (LocalDate) -> Unit,
    onGotoRoom: (Long) -> Unit,
    onGotoTeacher: (Long) -> Unit
) {
    DynamicScaffoldPortal()

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
                        state = state,
                        onDateSelect = onDateSelect,
                        onGotoRoom = onGotoRoom,
                        onGotoTeacher = onGotoTeacher
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