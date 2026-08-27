package org.kaorun.kadai.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kaorun.kadai.data.repository.TaskRepository
import org.kaorun.kadai.reminder.data.ScheduledNotificationRepository
import org.kaorun.kadai.reminder.impl.NotificationAlarmScheduler
import org.kaorun.kadai.reminder.impl.NotificationNotifier
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationRepository: ScheduledNotificationRepository

    @Inject
    lateinit var taskRepository: TaskRepository

    companion object {
        const val ACTION_MARK_COMPLETED = "org.kaorun.kadai.ACTION_MARK_COMPLETED"
        const val EXTRA_TASK_ID = "extra_task_id"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val pending = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                when (intent.action) {
                    ACTION_MARK_COMPLETED -> {
                        val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1L)
                        val messageId = intent.getLongExtra(
                            NotificationAlarmScheduler.NOTIFICATION_ID,
                            -1L
                        )

                        if (taskId != -1L) {
                            taskRepository.getById(taskId)?.let { task ->
                                taskRepository.update(task.copy(isCompleted = true))
                            }
                        }

                        if (messageId != -1L) {
                            NotificationManagerCompat.from(context).cancel(messageId.toInt())
                            notificationRepository.markCompleted(messageId)
                        }
                    }

                    else -> {
                        val messageId = intent.getLongExtra(
                            NotificationAlarmScheduler.NOTIFICATION_ID,
                            -1L
                        )
                        if (messageId == -1L) return@runCatching

                        val notification = notificationRepository.getById(messageId) ?: return@runCatching
                        if (notification.isSent) return@runCatching

                        NotificationNotifier(
                            context = context,
                            messageId = messageId,
                            taskId = notification.taskId,
                            title = notification.title,
                            content = notification.details
                        ).showNotification()

                        notificationRepository.markSent(messageId)
                    }
                }
            }.also {
                pending.finish()
            }
        }
    }
}