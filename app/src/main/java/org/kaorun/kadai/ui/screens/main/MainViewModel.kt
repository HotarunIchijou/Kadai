package org.kaorun.kadai.ui.screens.main

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kaorun.kadai.R
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.data.TaskSortConfig
import org.kaorun.kadai.data.TaskSortField
import org.kaorun.kadai.data.repository.TaskRepository
import org.kaorun.kadai.data.repository.UserPreferencesRepository
import org.kaorun.kadai.reminder.AlarmScheduler
import org.kaorun.kadai.reminder.data.ScheduledNotificationRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val scheduledNotificationRepository: ScheduledNotificationRepository,
    private val alarmScheduler: AlarmScheduler
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

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val mainUiState: StateFlow<MainUiState> = _searchQuery
        .map { it.trim() }
        .distinctUntilChanged()
        .combine(sortConfig) { query, config -> query to config }
        .flatMapLatest { (query, config) ->
            repository.getAllSorted(query, config.field, config.direction)
        }
        .map<List<Task>, MainUiState> { tasks ->
            withContext(Dispatchers.Default) {
                val (pending, completed) = tasks.partition { !it.isCompleted }
                MainUiState.Success(
                    pendingTasks = pending,
                    completedTasks = completed
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainUiState.Loading
        )

    fun onSortFieldSelected(field: TaskSortField) {
        viewModelScope.launch {
            val config = sortConfig.value.clickHandler(newField = field)
            userPreferencesRepository.updateSortConfig(config)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onTaskCompletionToggled(task: Task, isCompleted: Boolean) {
        val messageResId = if (isCompleted) {
            R.string.snackbar_message_completed
        } else {
            R.string.snackbar_message_uncompleted
        }
        viewModelScope.launch {
            task.dueTimestamp?.let {
                cancelNotification(task.id)
            }
            repository.update(task.copy(isCompleted = isCompleted))
            showSnackbarMessage(messageResId, task, previousState = !isCompleted)
        }
    }

    fun onUndoTaskCompletion(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.update(task.copy(isCompleted = isCompleted))
        }
    }

    fun onSnackbarMessageDismissed() {
        _snackbarMessage.value = null
    }

    fun onPermissionDismissed() {
        viewModelScope.launch {
            userPreferencesRepository.setPermissionCardDismissed()
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

    private suspend fun cancelNotification(taskId: Long) {
        val existing = scheduledNotificationRepository.getByTaskId(taskId)
        existing?.let { alarmScheduler.cancel(it) }
        scheduledNotificationRepository.deleteByTaskId(taskId)
    }
}