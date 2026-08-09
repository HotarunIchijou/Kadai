package org.kaorun.kadai.reminder

import org.kaorun.kadai.reminder.data.ScheduledNotification

interface AlarmScheduler {
    fun schedule(notification: ScheduledNotification)
    fun cancel(notification: ScheduledNotification)
}