package org.kaorun.kadai.di

import android.content.Context
import androidx.room.Room
import org.kaorun.kadai.data.TaskDao
import org.kaorun.kadai.data.TaskDatabase
import org.kaorun.kadai.data.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideNoteDao(db: TaskDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideRepository(dao: TaskDao): TaskRepository = TaskRepository(dao)
}