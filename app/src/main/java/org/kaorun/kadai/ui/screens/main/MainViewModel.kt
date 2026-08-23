@file:OptIn(FlowPreview::class)

package org.kaorun.kadai.ui.screens.main

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.data.SortDirection
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.data.TaskSortBy
import org.kaorun.kadai.data.TaskSortConfig
import org.kaorun.kadai.data.repository.TaskRepository
import org.kaorun.kadai.data.repository.UserPreferencesRepository
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    data class TaskSnackbarMessage(
        val id: Long,
        @StringRes val messageResId: Int,
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

    val sortConfig: StateFlow<TaskSortConfig> = userPreferencesRepository.sortConfig
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskSortConfig()
        )

    val tasks: StateFlow<List<Task>> = combine(
        repository.allTasks,
        _searchQuery
            .debounce(300.milliseconds)
            .distinctUntilChanged(),
        sortConfig
    ) { tasks, query, config ->
        val trimmed = query.trim()
        val filtered = if (trimmed.isBlank()) {
            tasks
        } else {
            tasks.filter { task ->
                task.title.contains(trimmed, ignoreCase = true) ||
                task.details.orEmpty().contains(trimmed, ignoreCase = true)
            }
        }

        when (config.sortBy) {
            TaskSortBy.DATE_CREATED -> when (config.direction) {
                SortDirection.ASCENDING -> filtered.sortedBy { it.id }
                SortDirection.DESCENDING -> filtered.sortedByDescending { it.id }
            }

            TaskSortBy.TITLE -> {
                val comparator = compareBy(
                    comparator = String.CASE_INSENSITIVE_ORDER,
                    selector = Task::title
                ).thenBy { it.id }
                when (config.direction) {
                    SortDirection.ASCENDING -> filtered.sortedWith(comparator)
                    SortDirection.DESCENDING -> filtered.sortedWith(comparator.reversed())
                }
            }

            TaskSortBy.DATE_REMINDER -> {
                val nullsLast = compareBy<Task> { it.timestamp == null }
                when (config.direction) {
                    SortDirection.ASCENDING -> filtered.sortedWith(
                        nullsLast
                            .thenBy { it.timestamp
                            }.thenBy { it.id }
                    )
                    SortDirection.DESCENDING -> filtered.sortedWith(
                        nullsLast
                            .thenByDescending { it.timestamp }
                            .thenByDescending { it.id }
                    )
                }
            }
        }
    }
    .flowOn(Dispatchers.Default)
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSortByClick(sortBy: TaskSortBy) {
        viewModelScope.launch {
            val updatedConfig = sortConfig.value.clickHandler(newSortBy = sortBy)
            userPreferencesRepository.updateSortConfig(updatedConfig)
        }
    }

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