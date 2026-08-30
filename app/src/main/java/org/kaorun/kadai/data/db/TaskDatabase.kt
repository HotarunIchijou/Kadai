package org.kaorun.kadai.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.kaorun.kadai.data.dao.RecentSearchDao
import org.kaorun.kadai.data.dao.TaskDao
import org.kaorun.kadai.data.entity.RecentSearch
import org.kaorun.kadai.data.entity.Task
import org.kaorun.kadai.reminder.data.ScheduledNotification
import org.kaorun.kadai.reminder.data.ScheduledNotificationDao

@Database(
    entities = [Task::class, ScheduledNotification::class, RecentSearch::class],
    version = 2,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun scheduledNotificationDao(): ScheduledNotificationDao
    abstract fun recentSearchDao(): RecentSearchDao
}