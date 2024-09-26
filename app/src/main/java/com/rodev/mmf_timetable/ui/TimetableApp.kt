package com.rodev.mmf_timetable.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rodev.mmf_timetable.UserDataUiState
import com.rodev.mmf_timetable.feature.timetable.R
import com.rodev.mmf_timetable.feature.timetable.TimetableUiState
import com.rodev.mmf_timetable.navigation.TimetableNavHost

@Composable
fun TimetableApp(appState: TimetableAppState) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()

        Scaffold { paddings ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            ) {
                val destination = appState.currentDestination
                if (destination != null) {
                    TimetableTopAppBar(
                        title = stringResource(destination.id),
                        onMenuButtonClick = {}
                    )
                }

                TimetableNavHost(appState = appState)
            }
        }

//        Drawer(
//            drawerState = drawerState,
//            userDataState = userDataState,
//            onUserDataUpdate = viewModel::updateUserData
//        ) {
////                        TimetableScreenRoute(
////                            modifier = Modifier.fillMaxSize(),
////                            viewModel = viewModel,
////                            onDrawerOpen = {
////                                coroutineScope.launch {
////                                    drawerState.open()
////                                }
////                            }
////                        )
//        }
    }
}

@Composable
private fun Drawer(
    drawerState: DrawerState,
    userDataState: UserDataUiState,
    onUserDataUpdate: (course: Int, groupId: String, subGroup: String?) -> Unit,
    content: @Composable () -> Unit
) {
    var dialogOpen by remember { mutableStateOf(false) }

    if (dialogOpen && userDataState is UserDataUiState.Success) {
        CourseEditDialog(
            onDismiss = { dialogOpen = false },
            state = userDataState
        ) { course, group, subGroup ->
            onUserDataUpdate(course, group.id, subGroup)
            dialogOpen = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                state = userDataState,
                onCourseEditDialogOpen = { dialogOpen = true },
                onGotoSettings = {}
            )
        },
        content = content
    )
}
