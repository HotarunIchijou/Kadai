package org.kaorun.kadai.data

import androidx.room.Database
import androidx.room.RoomDatabase
import org.kaorun.kadai.reminder.data.ScheduledNotification
import org.kaorun.kadai.reminder.data.ScheduledNotificationDao

@Database(entities = [Task::class, ScheduledNotification::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun scheduledNotificationDao(): ScheduledNotificationDao
}