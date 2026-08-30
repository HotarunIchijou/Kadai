package org.kaorun.kadai.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.kaorun.kadai.data.dao.RecentSearchDao
import org.kaorun.kadai.data.dao.TaskDao
import org.kaorun.kadai.data.db.TaskDatabase
import org.kaorun.kadai.data.repository.RecentSearchRepository
import org.kaorun.kadai.data.repository.TaskRepository
import org.kaorun.kadai.data.repository.UserPreferencesRepository
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
    )
        .fallbackToDestructiveMigration(dropAllTables = false)
        .build()

    @Provides
    fun provideTaskDao(db: TaskDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideScheduledNotificationDao(db: TaskDatabase): ScheduledNotificationDao =
        db.scheduledNotificationDao()

    @Provides
    fun provideRecentSearchDao(db: TaskDatabase): RecentSearchDao = db.recentSearchDao()

    @Provides
    fun provideTaskRepository(dao: TaskDao): TaskRepository = TaskRepository(dao)

    @Provides
    fun provideScheduledNotificationRepository(
        dao: ScheduledNotificationDao
    ): ScheduledNotificationRepository = ScheduledNotificationRepository(dao)

    @Provides
    fun provideRecentSearchRepository(
        dao: RecentSearchDao
    ): RecentSearchRepository = RecentSearchRepository(dao)

    @Provides
    fun provideUserPreferencesRepository(
        dataStore: DataStore<Preferences>
    ): UserPreferencesRepository = UserPreferencesRepository(dataStore)
}