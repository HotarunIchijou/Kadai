package org.kaorun.kadai.ui.utils

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.material3.TimePickerState
import org.kaorun.kadai.R
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale

private val systemZone = ZoneId.systemDefault()

fun LocalDate.toTimestamp(state: TimePickerState): Long {
    return this.atTime(state.hour, state.minute)
        .atZone(systemZone)
        .toInstant()
        .toEpochMilli()
}

fun Long.toFormattedTime(context: Context): String {
    val time = Instant.ofEpochMilli(this).atZone(systemZone).toLocalTime()
    val pattern = if (DateFormat.is24HourFormat(context)) "HH:mm" else "h:mm a"

    return time.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
}

fun Long.toFormattedDate(context: Context): String {
    val date = Instant.ofEpochMilli(this).atZone(systemZone).toLocalDate()
    val today = LocalDate.now(systemZone)

    val todayLabel = context.getString(R.string.today)
    val tomorrowLabel = context.getString(R.string.tomorrow)
    val yesterdayLabel = context.getString(R.string.yesterday)

    return when (date) {
        today -> todayLabel
        today.plusDays(1) -> tomorrowLabel
        today.minusDays(1) -> yesterdayLabel
        else -> {
            val pattern = if (date.year == today.year) "MMM d" else "MMM d, yyyy"
            date.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
        }
    }
}

fun combineDateAndTime(dateMillis: Long?, timeState: TimePickerState): Long {
    val date = dateMillis?.let {
        Instant.ofEpochMilli(it).atZone(systemZone).toLocalDate()
    } ?: LocalDate.now(systemZone)

    return date.toTimestamp(timeState)
}