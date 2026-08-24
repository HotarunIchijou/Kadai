package org.kaorun.kadai.ui.screens.task

sealed interface TaskUiState {
    data object Loading : TaskUiState

    data class Success(
        val id: Long? = null,
        val title: String = "",
        val details: String? = null,
        val createdAtTimestamp: Long = System.currentTimeMillis(),
        val modifiedAtTimestamp: Long? = null,
        val dueTimestamp: Long? = null,
        val isDone: Boolean = false,
        val isNewTask: Boolean = false
    ) : TaskUiState
}