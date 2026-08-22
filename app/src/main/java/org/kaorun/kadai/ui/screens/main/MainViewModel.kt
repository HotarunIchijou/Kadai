package org.kaorun.kadai.ui.screens.main

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.data.repository.TaskRepository
import org.kaorun.kadai.data.repository.UserPreferencesRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    data class TaskSnackbarMessage(
        val id: Long,
        @param:StringRes val messageResId: Int,
        val task: Task,
        val previousCompletedState: Boolean
    )

    private val _searchQuery = MutableStateFlow("")
    private val _snackbarMessage = MutableStateFlow<TaskSnackbarMessage?>(null)
    val snackbarMessage: StateFlow<TaskSnackbarMessage?> = _snackbarMessage.asStateFlow()
    val isPermissionCardDismissed: StateFlow<Boolean> = userPreferencesRepository
        .isPermissionCardDismissed
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val tasks: StateFlow<List<Task>> = repository.allTasks
        .combine(_searchQuery) { tasks, query ->
            if (query.isBlank()) tasks
            else tasks.filter { task ->
                task.title.contains(query, ignoreCase = true) ||
                        task.details.orEmpty().contains(query, ignoreCase = true)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onTaskCheck(task: Task, isCompleted: Boolean) {
        val messageResId = if (isCompleted) R.string.snackbar_message_completed
        else R.string.snackbar_message_uncompleted

        viewModelScope.launch {
            repository.update(task.copy(isCompleted = isCompleted))
            showSnackbarMessage(messageResId, task, previousState = !isCompleted)
        }
    }

    private fun showSnackbarMessage(
        @StringRes messageResId: Int,
        task: Task,
        previousState: Boolean
    ) {
        _snackbarMessage.value = TaskSnackbarMessage(
            id = UUID.randomUUID().mostSignificantBits,
            messageResId = messageResId,
            task = task,
            previousCompletedState = previousState
        )
    }

    fun onUndoSnackbar(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            runCatching {
                repository.update(task.copy(isCompleted = isCompleted))
            }
        }
    }

    fun snackbarMessageShown() {
        _snackbarMessage.value = null
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onPermissionCardDismiss() {
        viewModelScope.launch {
            userPreferencesRepository.setPermissionCardDismissed()
        }
    }
}