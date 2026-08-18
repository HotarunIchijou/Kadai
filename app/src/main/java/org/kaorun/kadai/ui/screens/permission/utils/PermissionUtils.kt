package org.kaorun.kadai.ui.screens.permission.utils

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

internal fun notificationPermissionMissing(context: Context): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED

internal fun exactAlarmPermissionMissing(context: Context): Boolean =
    context.getSystemService(AlarmManager::class.java)?.canScheduleExactAlarms() == false