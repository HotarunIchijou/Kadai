package org.kaorun.kadai.reminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

abstract class Notifier(
    protected val context: Context
) {
    abstract val notificationId: Long
    abstract val channelId: String
    abstract val channelName: String
    abstract val channelImportance: Int

    protected fun createNotificationChannel() {
        val channel = NotificationChannel(channelId, channelName, channelImportance)
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    protected fun baseBuilder(): NotificationCompat.Builder {
        createNotificationChannel()
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    protected fun notify(builder: NotificationCompat.Builder) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(notificationId.toInt(), builder.build())
    }
}