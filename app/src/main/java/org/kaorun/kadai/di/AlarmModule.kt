package org.kaorun.kadai.di

import android.app.AlarmManager
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.kaorun.kadai.reminder.AlarmScheduler
import org.kaorun.kadai.reminder.impl.NotificationAlarmScheduler

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmModule {

    @Binds
    abstract fun bindAlarmScheduler(impl: NotificationAlarmScheduler): AlarmScheduler

    companion object {
        @Provides
        fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
}