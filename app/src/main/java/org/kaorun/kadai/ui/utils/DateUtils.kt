package org.kaorun.kadai.ui.utils

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.material3.TimePickerState
import org.kaorun.kadai.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val systemZone: ZoneId
    get() = ZoneId.systemDefault()

fun LocalDate.toTimestamp(
    state: TimePickerState,
    zoneId: ZoneId = systemZone
): Long {
    return this.atTime(state.hour, state.minute)
        .atZone(zoneId)
        .toInstant()
        .toEpochMilli()
}

fun Long.toFormattedTime(context: Context): String =
    DateFormat.getTimeFormat(context).format(this)

fun Long.toFormattedDate(
    context: Context,
    zoneId: ZoneId = systemZone
): String {
    val date = Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()
    val today = LocalDate.now(zoneId)

    return when (date) {
        today -> context.getString(R.string.today)
        today.plusDays(1) -> context.getString(R.string.tomorrow)
        today.minusDays(1) -> context.getString(R.string.yesterday)
        else -> {
            val locale = Locale.getDefault()
            val skeleton = if (date.year == today.year) "MMMd" else "yMMMd"
            val pattern = DateFormat.getBestDateTimePattern(locale, skeleton)
            val formatter = DateTimeFormatter.ofPattern(pattern, locale)

            date.format(formatter)
        }
    }
}

fun Long.toFormattedFullDateTime(context: Context): String {
    val dateStr = DateFormat.getMediumDateFormat(context).format(this)
    val timeStr = DateFormat.getTimeFormat(context).format(this)
    return "$dateStr, $timeStr"
}

fun combineDateAndTime(
    dateMillis: Long?,
    timeState: TimePickerState,
    isUtcDatePickerMillis: Boolean = false,
    zoneId: ZoneId = systemZone
): Long {
    val date = dateMillis?.let {
        if (isUtcDatePickerMillis) {
            Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
        } else {
            Instant.ofEpochMilli(it).atZone(zoneId).toLocalDate()
        }
    } ?: LocalDate.now(zoneId)

    return date.toTimestamp(timeState, zoneId)
}