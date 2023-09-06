package io.github.krxwallo.enter.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import io.github.krxwallo.enter.R
import io.github.krxwallo.enter.TAG
import io.github.krxwallo.enter.defaultDoorId
import io.github.krxwallo.enter.dto.Device
import io.github.krxwallo.enter.dto.DoorState
import io.github.krxwallo.enter.net.APIClient
import kotlinx.coroutines.launch

private const val ACTION_OPEN_DOOR = "io.github.krxwallo.enter.action.OPEN_DOOR"

/**
 * Simple widget for opening the door.
 */
class OpenDoorWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == ACTION_OPEN_DOOR) {
            // Handle the button click action here
            Log.i(TAG, "onReceive: Open door button clicked! Opening door...")
            APIClient.scope.launch {
                APIClient.setDoorState(Device(iseId = defaultDoorId), DoorState.OPEN)
            }
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val intent = Intent(context, OpenDoorWidget::class.java)
    intent.action = ACTION_OPEN_DOOR
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.open_door_widget)
    views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)

    Log.i(TAG, "updateAppWidget: Created widget")
}