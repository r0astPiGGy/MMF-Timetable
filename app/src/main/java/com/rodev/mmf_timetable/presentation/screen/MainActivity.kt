package com.rodev.mmf_timetable.presentation.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rodev.mmf_timetable.presentation.screen.timetable.TimetableScreen
import com.rodev.mmf_timetable.presentation.screen.timetable.TimetableViewModel
import com.rodev.mmf_timetable.presentation.screen.timetable.components.CourseEditDialog
import com.rodev.mmf_timetable.presentation.screen.timetable.components.DrawerContent
import com.rodev.mmf_timetable.presentation.theme.MMF_TimetableTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MMF_TimetableTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val viewModel: MainViewModel = hiltViewModel()
                    val userDataState by viewModel.state.collectAsStateWithLifecycle()

                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val coroutineScope = rememberCoroutineScope()

                    Drawer(
                        drawerState = drawerState,
                        userDataState = userDataState,
                        onUserDataUpdate = viewModel::updateUserData
                    ) {
                        TimetableScreenRoute(
                            modifier = Modifier.fillMaxSize(),
                            onDrawerOpen = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimetableScreenRoute(
    modifier: Modifier = Modifier,
    onDrawerOpen: () -> Unit,
    viewModel: TimetableViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TimetableScreen(
        modifier = modifier,
        state = state,
        onMenuButtonClick = onDrawerOpen
    )
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
