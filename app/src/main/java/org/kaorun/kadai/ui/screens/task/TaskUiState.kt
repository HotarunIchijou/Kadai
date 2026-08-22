package org.kaorun.kadai.ui.screens.task

sealed interface TaskUiState {
    data object Loading : TaskUiState

    data class Success(
        val id: Long? = null,
        val title: String = "",
        val details: String? = null,
        val timestamp: Long? = null,
        val isDone: Boolean = false
    ) : TaskUiState
}