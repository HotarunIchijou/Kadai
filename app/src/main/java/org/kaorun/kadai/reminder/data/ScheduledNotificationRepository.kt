package org.kaorun.kadai.reminder.data

class ScheduledNotificationRepository(private val dao: ScheduledNotificationDao) {
    suspend fun insert(notification: ScheduledNotification): Long {
        return dao.insert(notification)
    }

    suspend fun markSent(id: Long) = dao.markSent(id)

    suspend fun markCompleted(id: Long) = dao.markCompleted(id)

    suspend fun delete(notification: ScheduledNotification): Int {
        return dao.delete(notification)
    }

    suspend fun getById(id: Long): ScheduledNotification? = dao.getById(id)

    suspend fun getByTaskId(taskId: Long): ScheduledNotification? = dao.getByTaskId(taskId)

    suspend fun deleteByTaskId(taskId: Long) = dao.deleteByTaskId(taskId)

    suspend fun getPendingNotifications(): List<ScheduledNotification> = dao.getPendingNotifications()
}