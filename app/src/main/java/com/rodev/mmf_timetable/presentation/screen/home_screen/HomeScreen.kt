package com.rodev.mmf_timetable.presentation.screen.home_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val scaffoldState = rememberScaffoldState(
        drawerState = drawerState,
        snackbarHostState = snackbarHostState
    )
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
            onEvent(HomeScreenEvent.FetchTimetable)
        }
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TimetableTopAppBar(
                title = stringResource(R.string.timetable),
                onMenuButtonClick = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            )
        },
        drawerContent = {
            DrawerContent(
                modifier = Modifier.fillMaxWidth(),
                userInfo = state.userInfo,
                onCourseEditDialogOpen = {
                    onEvent(HomeScreenEvent.OpenCourseEditDialog)
                },
                onGotoSettings = onGotoSettings
            )
        }
    ) { paddings ->
        Timetable(
            modifier = Modifier.padding(paddings),
            state = state
        )
    }
}