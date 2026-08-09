package org.kaorun.kadai.reminder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheduled_messages")
data class ScheduledNotification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: Long,
    val title: String,
    val details: String?,
    val triggerAtMillis: Long,
    val isSent: Boolean = false
)