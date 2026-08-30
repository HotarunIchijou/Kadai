package org.kaorun.kadai.ui.screens.settings.about.utils

import org.kaorun.kadai.BuildConfig

fun getAppVersion(): String = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"