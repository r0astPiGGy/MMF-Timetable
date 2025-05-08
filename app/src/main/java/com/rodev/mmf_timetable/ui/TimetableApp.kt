package com.rodev.mmf_timetable.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rodev.mmf_timetable.K
import com.rodev.mmf_timetable.core.ui.DynamicScaffold
import com.rodev.mmf_timetable.navigation.TimetableNavHost
import com.rodev.mmf_timetable.navigation.TopLevelDestination
import com.rodev.mmf_timetable.widget.TimetableWidgetReceiver
import com.rodev.mmf_timetable.widget.requestPinGlanceWidget
import kotlinx.coroutines.launch


@Composable
fun TimetableApp(appState: TimetableAppState) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(appState.currentDestination) {
            coroutineScope.launch {
                drawerState.close()
            }
        }

        DynamicScaffold(
            key = appState.currentDestination,
            bottomBar = {
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(),
                    visible = TopLevelDestination.entries.any { appState.topLevelDestination == it }
                ) {
                    BottomAppBar(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val destination = appState.topLevelDestination
                        TopLevelDestination.entries.forEach { item ->
                            val isSelected = remember(destination) {
                                destination == item
                            }

                            NavigationBarItem(
                                selected = isSelected,
                                label = { Text(stringResource(item.titleTextId)) },
                                icon = {
                                    if (isSelected) {
                                        Icon(item.iconSelected, contentDescription = item.name)
                                    } else {
                                        Icon(item.icon, contentDescription = item.name)
                                    }
                                },
                                onClick = {
                                    appState.navigateToTopLevelDestination(item)
                                }
                            )
                        }
                    }
                }
            },
            topAppBar = { }
        ) { paddings ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            ) {
                TimetableNavHost(appState = appState)
            }
        }
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

