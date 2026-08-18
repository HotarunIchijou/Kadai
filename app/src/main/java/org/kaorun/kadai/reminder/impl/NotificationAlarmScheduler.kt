package org.kaorun.kadai.reminder.impl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.kaorun.kadai.reminder.receiver.AlarmReceiver
import org.kaorun.kadai.reminder.AlarmScheduler
import org.kaorun.kadai.reminder.data.ScheduledNotification
import javax.inject.Inject

class NotificationAlarmScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager
    ) : AlarmScheduler {
    companion object {
        const val NOTIFICATION_ID = "NOTIFICATION_ID"
    }

    override fun schedule(notification: ScheduledNotification) {
        if (notification.triggerAtMillis <= System.currentTimeMillis() ||
            notification.isSent ||
            notification.isCompleted) { return }

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notification.triggerAtMillis,
                createPendingIntent(notification)
            )
        } catch (e: SecurityException) {
            throw ExactAlarmPermissionMissingException(e)
        }
    }

    override fun cancel(notification: ScheduledNotification) {
        val pendingIntent = createPendingIntent(notification)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun createPendingIntent(notification: ScheduledNotification): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(NOTIFICATION_ID, notification.id)
        }
        return PendingIntent.getBroadcast(
            context,
            notification.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    class ExactAlarmPermissionMissingException(e: Throwable? = null) : Exception(e)
}