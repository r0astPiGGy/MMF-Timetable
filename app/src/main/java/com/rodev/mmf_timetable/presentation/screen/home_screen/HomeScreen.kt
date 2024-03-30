package com.rodev.mmf_timetable.presentation.screen.home_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rodev.mmf_timetable.R
import com.rodev.mmf_timetable.presentation.screen.home_screen.components.CourseEditDialog
import com.rodev.mmf_timetable.presentation.screen.home_screen.components.DrawerContent
import com.rodev.mmf_timetable.presentation.screen.home_screen.components.Timetable
import com.rodev.mmf_timetable.presentation.screen.home_screen.components.TimetableTopAppBar
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.HomeScreenEvent
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.HomeScreenResult
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.HomeScreenState
import kotlinx.coroutines.launch

@Composable
@Preview
private fun HomeScreenPreview() {
    HomeScreen(
        state = HomeScreenState(),
        onEvent = {},
        onGotoSettings = {}
    )
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onGotoSettings: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onGotoSettings = onGotoSettings
    )
}

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit,
    onGotoSettings: () -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        onEvent(HomeScreenEvent.FetchTimetable)
    }

    LaunchedEffect(state.result) {
        if (state.result is HomeScreenResult.Error) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(state.result.message)
            }
            state.result.consume()
        }
    }

    CourseEditDialog(
        state = state,
        presented = state.courseEditDialogOpened,
        onDismiss = {
            onEvent(HomeScreenEvent.CloseCourseEditDialog)
        },
        onCourseAndGroupSelected = { course, group, subGroup ->
            onEvent(HomeScreenEvent.CourseAndGroupSelected(course, group, subGroup))
        }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                userInfo = state.userInfo,
                onCourseEditDialogOpen = {
                    onEvent(HomeScreenEvent.OpenCourseEditDialog)
                },
                onGotoSettings = onGotoSettings
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TimetableTopAppBar(
                    title = stringResource(R.string.timetable),
                    subTitle = state.currentStudyWeek?.let { stringResource(R.string.current_week, it) },
                    onMenuButtonClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        ) { paddings ->
            Timetable(
                modifier = Modifier.padding(paddings),
                state = state
            )
        }
    }
}