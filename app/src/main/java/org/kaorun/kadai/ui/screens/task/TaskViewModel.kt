package org.kaorun.kadai.ui.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.data.repository.TaskRepository
import org.kaorun.kadai.reminder.AlarmScheduler
import org.kaorun.kadai.reminder.data.ScheduledNotification
import org.kaorun.kadai.reminder.data.ScheduledNotificationRepository
import javax.inject.Inject
import kotlin.time.Clock

@OptIn(FlowPreview::class)
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val scheduledNotificationRepository: ScheduledNotificationRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val mutex = Mutex()
    private val _uiState = MutableStateFlow<TaskUiState>(TaskUiState.Loading)
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private enum class LoadState { NOT_LOADED, LOADING, LOADED }
    private var loadState = LoadState.NOT_LOADED
    private var isDeleted = false

    init {
        _uiState
            .filterIsInstance<TaskUiState.Success>()
            .distinctUntilChanged { old, new ->
                old.title == new.title &&
                old.details == new.details &&
                old.timestamp == new.timestamp &&
                old.isDone == new.isDone
            }
            .debounce(400)
            .onEach { if (!isDeleted) save() }
            .launchIn(viewModelScope)
    }

    fun load(taskId: Long?) {
        if (loadState != LoadState.NOT_LOADED) return

        if (taskId == null || taskId == 0L) {
            _uiState.value = TaskUiState.Success()
            loadState = LoadState.LOADED
            return
        }

        loadState = LoadState.LOADING
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId)
            _uiState.value = task?.let {
                TaskUiState.Success(
                    id = task.id,
                    title = task.title,
                    details = task.details,
                    timestamp = task.timestamp,
                    isDone = task.isCompleted
                )
            } ?: run { TaskUiState.Success() }
            loadState = LoadState.LOADED
        }
    }

    fun onBack(navigateBack: () -> Unit) {
        if (!isDeleted && _uiState.value is TaskUiState.Success) {
            viewModelScope.launch {
                save()
                navigateBack()
            }
        } else {
            navigateBack()
        }
    }

    fun onTitleChange(value: String) {
        _uiState.update { state ->
            if (state is TaskUiState.Success) state.copy(title = value)
            else state
        }
    }

    fun onDetailsChange(value: String) {
        _uiState.update { state ->
            if (state is TaskUiState.Success) state.copy(details = value)
            else state
        }
    }

    fun onDoneChange(value: Boolean) {
        _uiState.update { state ->
            if (state is TaskUiState.Success) state.copy(isDone = value)
            else state
        }
    }

    fun onTimestampChange(value: Long?) {
        val currentState = _uiState.value as? TaskUiState.Success ?: return
        if (value == currentState.timestamp) return

        _uiState.update { state ->
            if (state is TaskUiState.Success) state.copy(timestamp = value)
            else state
        }
        viewModelScope.launch { save() }
    }

    fun onDelete() {
        isDeleted = true
        viewModelScope.launch {
            mutex.withLock {
                val currentState = _uiState.value as? TaskUiState.Success ?: return@withLock
                val currentId = currentState.id
                if (currentId != null && currentId != 0L) {
                    cancelNotification(taskId = currentId)
                    taskRepository.delete(task = currentState.toTask())
                }
            }
        }
    }

    private suspend fun save() = mutex.withLock {
        val currentState = _uiState.value as? TaskUiState.Success ?: return@withLock
        with(currentState) {
            if (title.isBlank()) {
                if (id != null && id != 0L) {
                    cancelNotification(taskId = id)
                    taskRepository.delete(task = toTask())
                    _uiState.update { (it as? TaskUiState.Success)?.copy(id = null) ?: it }
                }
                return@withLock
            }

            val savedId = if (id == null || id == 0L) {
                val newId = taskRepository.insert(task = toTask())
                _uiState.update { (it as? TaskUiState.Success)?.copy(id = newId) ?: it }
                newId
            } else {
                taskRepository.update(task = toTask())
                id
            }

            if (timestamp != null && timestamp > Clock.System.now().toEpochMilliseconds()) {
                scheduleNotification(
                    taskId = savedId,
                    title = title,
                    details = details?.trim()?.takeIf { it.isNotEmpty() },
                    triggerAtMillis = timestamp
                )
            } else {
                cancelNotification(taskId = savedId)
            }
        }
    }

    private suspend fun scheduleNotification(
        taskId: Long,
        title: String,
        details: String?,
        triggerAtMillis: Long
    ) {
        cancelNotification(taskId = taskId)
        val notification = ScheduledNotification(
            taskId = taskId,
            title = title,
            details = details,
            triggerAtMillis = triggerAtMillis
        )
        val id = scheduledNotificationRepository.insert(notification)
        alarmScheduler.schedule(notification.copy(id = id))
    }

    private suspend fun cancelNotification(taskId: Long) {
        val existing = scheduledNotificationRepository.getByTaskId(taskId)
        existing?.let { alarmScheduler.cancel(it) }
        scheduledNotificationRepository.deleteByTaskId(taskId)
    }

    private fun TaskUiState.Success.toTask() = Task(
        id = id ?: 0L,
        title = title.trim(),
        details = details?.trim()?.takeIf { it.isNotEmpty() },
        timestamp = timestamp,
        isCompleted = isDone
    )
}