package org.kaorun.kadai.reminder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScheduledNotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: ScheduledNotification): Long

    @Query("UPDATE scheduled_messages SET isSent = 1 WHERE id = :id")
    suspend fun markSent(id: Long)

    @Delete
    suspend fun delete(notification: ScheduledNotification): Int

    @Query("SELECT * FROM scheduled_messages WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ScheduledNotification?

    @Query("SELECT * FROM scheduled_messages WHERE taskId = :taskId LIMIT 1")
    suspend fun getByTaskId(taskId: Long): ScheduledNotification?

    @Query("DELETE FROM scheduled_messages WHERE taskId = :taskId")
    suspend fun deleteByTaskId(taskId: Long)

    @Query("SELECT * FROM scheduled_messages WHERE isSent = 0")
    suspend fun getPendingNotifications(): List<ScheduledNotification>
}