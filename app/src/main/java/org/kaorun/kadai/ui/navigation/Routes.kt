package org.kaorun.kadai.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavRoute : NavKey

interface DeepLinkKey : NavRoute {
    val parent: NavRoute
}

@Serializable
data object NotificationPermissionRoute : NavRoute

@Serializable
data object MainRoute : NavRoute

@Serializable
data class TaskRoute(val taskId: Long) : DeepLinkKey {
    override val parent: NavRoute get() = MainRoute
}

@Serializable
data class AddTaskRoute(val instanceId: Long = System.currentTimeMillis()) : NavRoute

@Serializable
data object SettingsRoute : NavRoute

@Serializable
data object SettingsAppearanceRoute : NavRoute

@Serializable
data object SettingsAboutRoute : NavRoute