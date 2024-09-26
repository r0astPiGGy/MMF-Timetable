package com.rodev.mmf_timetable.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodev.mmf_timetable.K
import com.rodev.mmf_timetable.R
import com.rodev.mmf_timetable.UserDataUiState
import com.rodev.mmf_timetable.core.designsystem.component.DrawerNavItem
import com.rodev.mmf_timetable.core.designsystem.theme.MMF_TimetableTheme
import com.rodev.mmf_timetable.widget.TimetableWidgetReceiver
import com.rodev.mmf_timetable.widget.requestPinGlanceWidget
import kotlinx.coroutines.launch

@Preview
@Composable
private fun DrawerPreview() {
    MMF_TimetableTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
        ) {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
            val scaffoldState = rememberScaffoldState(drawerState)

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState,
                drawerContent = {
                    DrawerContent(
                        modifier = Modifier,
                        state = UserDataUiState.Success(
                            course = 1,
                            group = "Группа 9",
                            subGroup = null,
                        ),
                        onCourseEditDialogOpen = {},
                        onGotoSettings = {}
                    )
                }
            ) {
                Box(modifier = Modifier.padding(it)) {}
            }
        }
    }
}

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    state: UserDataUiState,
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
                .clickable(
                    enabled = state !is UserDataUiState.Loading,
                    onClick = onCourseEditDialogOpen
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
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

                when (state) {
                    UserDataUiState.Error -> Text(text = "Ошибка загрузки")
                    UserDataUiState.Loading -> Text(text = "Загрузка...")
                    is UserDataUiState.Success -> {
                        if (state.course == null && state.group == null) {
                            Text(text = "Курс и группа не выбраны")
                        } else {
                            val subGroup = state.subGroup?.let { " ($it)" } ?: ""
                            Text(text = "${state.course} курс, ${state.group}${subGroup}")
                        }
                    }
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