package com.example.weather.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import com.example.weather.R
import java.util.*

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.weather_widget)
    views.setTextViewText(R.id.temp_appwidget_text, "32")
    views.setTextViewText(R.id.place_appwidget_text, "NSK")
    views.setTextViewText(R.id.date_appwidget_text, Calendar.getInstance().time.toString())

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)

}