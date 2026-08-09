package org.kaorun.kadai.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kaorun.kadai.reminder.data.ScheduledNotificationRepository
import org.kaorun.kadai.reminder.impl.NotificationAlarmScheduler
import org.kaorun.kadai.reminder.impl.NotificationNotifier
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: ScheduledNotificationRepository

    override fun onReceive(context: Context, intent: Intent) {
        val messageId = intent.getLongExtra(
            NotificationAlarmScheduler.NOTIFICATION_ID,
            -1L
        )
        if (messageId == -1L) return

        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val notification = repository.getById(messageId) ?: return@launch
                if (notification.isSent) return@launch

                NotificationNotifier(
                    context = context,
                    messageId = messageId,
                    taskId = notification.taskId,
                    title = notification.title,
                    content = notification.details
                ).showNotification()
                repository.markSent(messageId)
            } finally {
                pending.finish()
            }
        }
    }
}