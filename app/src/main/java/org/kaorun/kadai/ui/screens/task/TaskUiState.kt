package org.kaorun.kadai.ui.screens.task

import androidx.compose.runtime.Immutable

@Immutable
data class TaskUiState(
    val id: Long = 0,
    val title: String = "",
    val details: String? = null,
    val timestamp: Long? = null,
    val isDone: Boolean = false
)