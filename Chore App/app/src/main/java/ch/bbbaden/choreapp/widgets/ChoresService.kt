package ch.bbbaden.choreapp.widgets

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder

class ChoresService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val allWidgetIds = intent?.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)
        allWidgetIds?.forEach {appWidgetId ->
            ChoresWidgetProvider.updateAppWidget(
                this,
                appWidgetManager,
                appWidgetId
            )
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
