package org.kaorun.kadai.reminder.impl

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.kaorun.kadai.MainActivity
import org.kaorun.kadai.reminder.Notifier
import androidx.core.net.toUri

class NotificationNotifier(
    context: Context,
    private val messageId: Long,
    private val taskId: Long,
    private val title: String,
    private val content: String?
) : Notifier(context) {
    override val notificationId: Long get() = messageId
    override val channelId: String get() = CHANNEL_ID
    override val channelName: String get() = "Scheduled messages"
    override val channelImportance: Int get() = NotificationManager.IMPORTANCE_HIGH

    companion object {
        const val CHANNEL_ID = "scheduled_messages_channel"
    }

    fun showNotification() {
        val deepLinkUri = "kadai://task/$taskId".toUri()
        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            setClass(context, MainActivity::class.java)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            messageId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = baseBuilder()
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)

        notify(builder)
    }
}