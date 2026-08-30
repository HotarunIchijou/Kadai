package org.kaorun.kadai.data.model

import androidx.annotation.StringRes
import org.kaorun.kadai.R

enum class ThemeMode(@StringRes val titleRes: Int) {
    SYSTEM_DEFAULT(R.string.theme_system_default),
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark)
}