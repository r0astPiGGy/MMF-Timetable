package com.rodev.mmf_timetable.presentation.screen.home_screen.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.rodev.mmf_timetable.K
import com.rodev.mmf_timetable.R
import com.rodev.mmf_timetable.domain.model.UserInfo
import com.rodev.mmf_timetable.presentation.theme.AppColors
import com.rodev.mmf_timetable.presentation.theme.MMF_TimetableTheme
import com.rodev.mmf_timetable.presentation.widget.TimetableWidget
import com.rodev.mmf_timetable.presentation.widget.TimetableWidgetReceiver
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
                        userInfo = null,
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
private fun NavItem(
    modifier: Modifier = Modifier,
    text: String,
    icon: Int,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        modifier = modifier,
        colors = NavigationDrawerItemDefaults.colors(
            unselectedTextColor = AppColors.Neutral,
            unselectedIconColor = AppColors.Neutral
        ),
        label = {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp
            )
        },
        selected = selected,
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        onClick = onClick
    )
}

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    userInfo: UserInfo?,
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
        Divider()

        Spacer(modifier = Modifier.size(10.dp))

        // TODO action list
        NavItem(
            text = stringResource(R.string.timetable_drawer_item),
            icon = R.drawable.calendar
        ) {}
        NavItem(
            text = stringResource(R.string.teachers_drawer_item),
            icon = R.drawable.user
        ) {}
        NavItem(
            text = stringResource(R.string.classrooms_drawer_item),
            icon = R.drawable.door
        ) {}

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        NavItem(
            text = stringResource(R.string.add_widget_drawer_item),
            icon = R.drawable.plus_square
        ) {
            coroutineScope.launch {
                requestAddWidget(context) // todo: move up a tree
            }
        }

        NavItem(
            text = stringResource(R.string.github_drawer_item),
            icon = R.drawable.github
        ) {
            openGithubProject(context) // todo: move up a tree
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        NavItem(
            text = stringResource(R.string.settings_drawer_item),
            icon = R.drawable.settings,
            onClick = onGotoSettings
        )
    }
//    Column(
//        modifier = modifier.padding(10.dp)
//    ) {
//            if (userInfo == null) {
//                Text(text = "Курс и группа не выбрана")
//            } else {
//                val subGroup = userInfo.subGroup?.let { "($it)" } ?: ""
//
//                Text(text = "${userInfo.course.course} курс, ${userInfo.group.name} $subGroup")
//            }
//
//            IconButton(
//                onClick = onCourseEditDialogOpen
//            ) {
//                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
//            }
//    }
}

private suspend fun requestAddWidget(context: Context) {
    try {
        GlanceAppWidgetManager(context)
            .requestPinGlanceAppWidget(
                TimetableWidgetReceiver::class.java,
                null,
            )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun openGithubProject(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(K.Constants.GITHUB_LINK))

    context.startActivity(intent)
}