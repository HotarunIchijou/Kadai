package org.kaorun.kadai.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute : NavKey

@Serializable
data class TaskRoute(val taskId: Long) : NavKey

@Serializable
data class AddTaskRoute(val instanceId: Long = System.currentTimeMillis()) : NavKey