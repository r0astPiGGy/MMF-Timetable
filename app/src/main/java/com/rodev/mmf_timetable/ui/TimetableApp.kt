package com.rodev.mmf_timetable.ui

import android.R.attr.onClick
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import com.rodev.mmf_timetable.K
import com.rodev.mmf_timetable.R
import com.rodev.mmf_timetable.core.designsystem.component.DrawerNavItem
import com.rodev.mmf_timetable.core.designsystem.component.TimetableTopAppBar
import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.model.data.UserData
import com.rodev.mmf_timetable.navigation.TimetableNavHost
import com.rodev.mmf_timetable.widget.TimetableWidgetReceiver
import com.rodev.mmf_timetable.widget.requestPinGlanceWidget
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@Composable
fun TimetableApp(appState: TimetableAppState) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()
        val userData by appState.userData.collectAsStateWithLifecycle()
        val courses by appState.courses.collectAsStateWithLifecycle()

        Drawer(
            drawerState = drawerState,
            userData = userData,
            courses = courses,
            onGroupSelected = appState::onGroupSelected
        ) {
            Scaffold { paddings ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddings)
                ) {
                    val destination = appState.currentDestination
                    if (destination != null) {
                        TimetableTopAppBar(
                            title = "TopAppBar",
                            onMenuButtonClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }

                    TimetableNavHost(appState = appState)
                }
            }
        }
    }
}

@Composable
private fun Drawer(
    drawerState: DrawerState,
    userData: UserData?,
    courses: List<Course>,
    onGroupSelected: (Int, String, String?) -> Unit,
    content: @Composable () -> Unit
) {
    var dialogOpen by remember { mutableStateOf(false) }

    val groupName: String? by remember {
        derivedStateOf {
            if (userData == null) return@derivedStateOf null

            val course = courses.firstOrNull {
                it.course == userData.course
            } ?: return@derivedStateOf null

            course.groups.firstOrNull { it.id == userData.groupId }?.name
        }
    }

    if (dialogOpen) {
        AlertDialog(onDismissRequest = {dialogOpen = false}, confirmButton = {}, text = {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(courses) {
                    Text(text = it.course.toString())
                    Column {
                        it.groups.forEach { g ->
                            TextButton(
                                onClick = {
                                    onGroupSelected(it.course, g.id, null)
                                    dialogOpen = false
                                }
                            ) {
                                Text(text = g.name)
                            }
                        }
                    }
                }
            }
        })
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                userData = userData,
                onCourseEditDialogOpen = { dialogOpen = true },
                onGotoSettings = {},
                groupName = groupName
            )
        },
        content = content
    )
}

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    userData: UserData?,
    groupName: String?,
    onGotoSettings: () -> Unit,
    onCourseEditDialogOpen: () -> Unit
) {
    DismissibleDrawerSheet(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.mmf_logo),
                contentDescription = "logo"
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .clickable(
                    onClick = onCourseEditDialogOpen
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Курс, группа",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.sp
                )

                if (userData == null) {
                    Text(text = "Курс и группа не выбраны")
                } else {
                    val subGroup = userData.subGroup?.let { " ($it)" } ?: ""
                    Text(text = "${userData.course} курс, ${groupName}${subGroup}")
                }
            }
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        }

        Spacer(modifier = Modifier.size(8.dp))

        Divider()

        Spacer(modifier = Modifier.size(8.dp))

        // TODO action list
        DrawerNavItem(
            text = stringResource(R.string.timetable_drawer_item),
            icon = R.drawable.calendar,
        ) {}
        DrawerNavItem(
            text = stringResource(R.string.teachers_drawer_item),
            icon = R.drawable.user
        ) {}
        DrawerNavItem(
            text = stringResource(R.string.classrooms_drawer_item),
            icon = R.drawable.door
        ) {}

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        DrawerNavItem(
            text = stringResource(R.string.add_widget_drawer_item),
            icon = R.drawable.plus_square
        ) {
            coroutineScope.launch {
                requestAddWidget(context) // todo: move up a tree
            }
        }

        DrawerNavItem(
            text = stringResource(R.string.github_drawer_item),
            icon = R.drawable.github
        ) {
            openGithubProject(context) // todo: move up a tree
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        DrawerNavItem(
            text = stringResource(R.string.settings_drawer_item),
            icon = R.drawable.settings,
            onClick = onGotoSettings
        )
    }
}

private suspend fun requestAddWidget(context: Context) {
    try {
        TimetableWidgetReceiver.requestPinGlanceWidget(context)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun openGithubProject(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(K.Constants.GITHUB_LINK))

    context.startActivity(intent)
}

//private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
//    this?.hierarchy?.any {
//        it.hasRoute(route)
//    } ?: false
