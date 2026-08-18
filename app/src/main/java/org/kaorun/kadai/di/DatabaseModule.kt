package org.kaorun.kadai.di

import android.content.Context
import androidx.room.Room
import org.kaorun.kadai.data.TaskDao
import org.kaorun.kadai.data.TaskDatabase
import org.kaorun.kadai.data.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.kaorun.kadai.reminder.data.ScheduledNotificationDao
import org.kaorun.kadai.reminder.data.ScheduledNotificationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase = Room.databaseBuilder(
        context = context,
        klass = TaskDatabase::class.java,
        name = "tasks_db"
    ).build()

    @Provides
    fun provideTaskDao(db: TaskDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideScheduledNotificationDao(db: TaskDatabase): ScheduledNotificationDao =
        db.scheduledNotificationDao()

    @Provides
    fun provideTaskRepository(dao: TaskDao): TaskRepository = TaskRepository(dao)

    @Provides
    fun provideScheduledNotificationRepository(dao: ScheduledNotificationDao) =
        ScheduledNotificationRepository(dao)
}