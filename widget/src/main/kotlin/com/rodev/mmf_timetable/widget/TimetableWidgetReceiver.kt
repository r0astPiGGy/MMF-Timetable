package com.rodev.mmf_timetable.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class TimetableWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = TimetableWidget()

    companion object
}

suspend fun TimetableWidgetReceiver.Companion.requestPinGlanceWidget(context: Context) {
    GlanceAppWidgetManager(context).requestPinGlanceAppWidget(TimetableWidgetReceiver::class.java, null)
}