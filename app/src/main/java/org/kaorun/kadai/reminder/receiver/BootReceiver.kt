package org.kaorun.kadai.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kaorun.kadai.reminder.AlarmScheduler
import org.kaorun.kadai.reminder.data.ScheduledNotificationRepository
import org.kaorun.kadai.reminder.impl.NotificationAlarmScheduler
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var repository: ScheduledNotificationRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            val pendingResult = goAsync()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val activeNotifications = repository.getPendingNotifications()

                    for (notification in activeNotifications) {
                        try {
                            alarmScheduler.schedule(notification)
                        } catch (_: NotificationAlarmScheduler.ExactAlarmPermissionMissingException) {
                            break
                        }
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}