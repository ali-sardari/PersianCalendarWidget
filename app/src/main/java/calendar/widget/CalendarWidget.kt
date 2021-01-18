package calendar.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.util.Log
import android.widget.RemoteViews
import calendar.widget.utils.PersianCalendar
import java.util.*

class CalendarWidget : AppWidgetProvider() {
    private lateinit var ids: IntArray

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        ids = appWidgetIds

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        Log.w("CalendarWidget", "onEnabled()")

        val alarmIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
//            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }.let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 1)
        }

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.setRepeating(
            AlarmManager.RTC,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
//            1000 * 60,
            alarmIntent
        )
    }

    override fun onDisabled(context: Context) {
        Log.w("CalendarWidget", "onDisabled()")

        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.cancel(PendingIntent.getBroadcast(context, 0, intent, 0))
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val action = intent!!.action
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE == action) {
            ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
                context?.let {
                    ComponentName(it, CalendarWidget::class.java)
                }
            )

            onUpdate(context!!, AppWidgetManager.getInstance(context), ids)
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val persianCalendar = PersianCalendar()
    val date = DateFormat.format("yyyy/MM/dd", Date())
    val time = DateFormat.format("HH:mm:ss", Date())

    val views = RemoteViews(context.packageName, R.layout.calendar_widget)

    views.setTextViewText(R.id.tv_jalali_date, persianCalendar.persianLongDate)
    views.setTextViewText(R.id.tv_gregorian_date, "$date  $time")

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}