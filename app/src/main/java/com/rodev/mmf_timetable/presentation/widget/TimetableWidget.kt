package com.rodev.mmf_timetable.presentation.widget

import android.content.Context
import android.widget.RemoteViews
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ColumnScope
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.rodev.mmf_timetable.R
import com.rodev.mmf_timetable.domain.model.WidgetState
import com.rodev.mmf_timetable.domain.use_case.GetWidgetStateUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

class TimetableWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode
        get() = SizeMode.Responsive(setOf(DpSize(500.dp, 500.dp)))

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetDependencyProvider {

        fun provideGetWrappedTimetableUseCase(): GetWidgetStateUseCase

    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val getWrappedTimetable = EntryPoints
            .get(context, WidgetDependencyProvider::class.java)
            .provideGetWrappedTimetableUseCase()

        // TODO observe as flow
        val timetable = getWrappedTimetable()

        provideContent {
            GlanceTheme {
                Content(timetable)
            }
        }
    }

    @Composable
    fun Content(state: WidgetState?) {
        when (state) {
            is WidgetState.OngoingLesson -> {
                CurrentLessonContent(state)
            }
            is WidgetState.NextLesson -> {
                NextLessonContent(state)
            }
            WidgetState.NoLesson -> {
                NoLessonContent()
            }
            null -> {
                NoDataProvidedContent()
            }
        }
    }

    @Composable
    fun CurrentLessonContent(state: WidgetState.OngoingLesson) {
        val entry = state.lesson

        WidgetScaffold(
            modifier = GlanceModifier
                .clickable(onClick = actionRunCallback<RefreshAction>()),
            headerText = "Текущая пара:",
            titleText = "${entry.subject} (${entry.classroom})",
            subTitleText = entry.teacher,
            footer = {
                Box(
                    modifier = GlanceModifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Text(
                            text = "Осталось ${state.minutesRemaining} минут",
                            style = TextStyle(
                                color = ColorProvider(Color.Gray)
                            )
                        )

                        Spacer(modifier = GlanceModifier.size(2.dp))

                        LinearProgressIndicator(
                            progress = state.progress,
                            modifier = GlanceModifier.fillMaxWidth()
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun NextLessonContent(state: WidgetState.NextLesson) {
        val entry = state.lesson

        WidgetScaffold(
            modifier = GlanceModifier
                .clickable(onClick = actionRunCallback<RefreshAction>()),
            headerText = "Следующая пара:",
            titleText = "${entry.subject} (${entry.classroom})",
            subTitleText = entry.teacher,
            footer = {
                Box(
                    modifier = GlanceModifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Spacer(modifier = GlanceModifier.size(2.dp))
                    Text(
                        modifier = GlanceModifier,
                        text = "Через ${state.minutesBeforeStart} минут",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                    )
                }
            }
        )
    }

    @Composable
    fun NoLessonContent() {
        WidgetColumn {
            Box(
                contentAlignment = Alignment.Center,
                modifier = GlanceModifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Пары закончились",
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )
            }
        }
    }

    @Composable
    fun NoDataProvidedContent() {
        WidgetColumn {
            Box(
                contentAlignment = Alignment.Center,
                modifier = GlanceModifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "No data",
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )
            }
        }
    }

    @Composable
    private fun LinearProgressIndicator(
        // From 0.0 to 1.0
        progress: Float,
        modifier: GlanceModifier = GlanceModifier
    ) {
        AndroidRemoteViews(
            remoteViews = RemoteViews(
                LocalContext.current.packageName,
                R.layout.widget_linear_progress_indicator
            ).apply {
                setProgressBar(R.id.widget_linear_progress_bar, 100, (progress * 100).toInt(), false)
            },
            modifier = modifier
        )
    }

    @Composable
    private fun WidgetScaffold(
        modifier: GlanceModifier = GlanceModifier,
        headerText: String,
        titleText: String,
        subTitleText: String,
        footer: @Composable () -> Unit
    ) {
        WidgetScaffold(
            modifier = modifier,
            header = {
                Text(headerText)
                Text(
                    text = titleText,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    )
                )
                Text(
                    text = subTitleText,
                    style = TextStyle(
                        color = ColorProvider(Color.Gray)
                    )
                )
            },
            footer = footer
        )
    }

    @Composable
    private fun WidgetScaffold(
        modifier: GlanceModifier = GlanceModifier,
        header: @Composable () -> Unit,
        footer: @Composable () -> Unit
    ) {
        WidgetColumn(
            modifier = modifier
                .fillMaxSize(),
        ) {
            header()
            Spacer(modifier = GlanceModifier.size(10.dp))
            footer()
        }
    }

    @Composable
    private fun WidgetColumn(
        modifier: GlanceModifier = GlanceModifier,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column(
            modifier = modifier
                .background(imageProvider = ImageProvider(R.drawable.app_widget_background))
                .padding(15.dp),
            content = content
        )
    }
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        TimetableWidget().updateAll(context)
    }

}