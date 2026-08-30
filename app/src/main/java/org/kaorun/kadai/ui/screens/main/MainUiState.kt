package org.kaorun.kadai.ui.screens.main

import org.kaorun.kadai.data.entity.Task

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(
        val pendingTasks: List<Task>,
        val completedTasks: List<Task>
    ) : MainUiState
}