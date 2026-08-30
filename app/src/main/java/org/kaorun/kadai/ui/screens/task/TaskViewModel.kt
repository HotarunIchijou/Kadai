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
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.kaorun.kadai.data.entity.Task
import org.kaorun.kadai.data.repository.TaskRepository
import org.kaorun.kadai.reminder.AlarmScheduler
import org.kaorun.kadai.reminder.data.ScheduledNotification
import org.kaorun.kadai.reminder.data.ScheduledNotificationRepository
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

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
    private var lastSavedTask: Task? = null
    private var isDeleted = false

    init {
        _uiState
            .filterIsInstance<TaskUiState.Success>()
            .distinctUntilChanged { old, new ->
                old.title == new.title &&
                    old.details == new.details &&
                    old.dueTimestamp == new.dueTimestamp &&
                    old.isDone == new.isDone
            }
            .drop(1)
            .debounce(300.milliseconds)
            .onEach { if (!isDeleted) save() }
            .launchIn(viewModelScope)
    }

    fun load(taskId: Long?) {
        if (loadState != LoadState.NOT_LOADED) return

        if (taskId == null || taskId == 0L) {
            lastSavedTask = null
            _uiState.value = TaskUiState.Success(isNewTask = true)
            loadState = LoadState.LOADED
            return
        }

        loadState = LoadState.LOADING
        viewModelScope.launch {
            val task = taskRepository.getById(taskId)
            lastSavedTask = task
            _uiState.value = task?.let {
                TaskUiState.Success(
                    id = task.id,
                    title = task.title,
                    details = task.details,
                    createdAtTimestamp = task.createdAtTimestamp,
                    modifiedAtTimestamp = task.modifiedAtTimestamp ?: task.createdAtTimestamp,
                    dueTimestamp = task.dueTimestamp,
                    isDone = task.isCompleted,
                    isNewTask = false
                )
            } ?: TaskUiState.Success(isNewTask = true)
            loadState = LoadState.LOADED
        }
    }

    fun onBack(navigateBack: () -> Unit) {
        if (!isDeleted && _uiState.value is TaskUiState.Success) {
            viewModelScope.launch {
                val currentState = _uiState.value as? TaskUiState.Success
                val isBlankTask = currentState?.title?.isBlank() == true &&
                    currentState.details.isNullOrBlank()

                if (isBlankTask && currentState.id != null && currentState.id != 0L) {
                    cancelNotification(taskId = currentState.id)
                    taskRepository.delete(task = currentState.toTask())
                } else if (!isBlankTask) {
                    save()
                }
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
            if (state is TaskUiState.Success) {
                state.dueTimestamp?.let {
                    viewModelScope.launch {
                        cancelNotification(state.toTask().id)
                    }
                }
                state.copy(isDone = value)
            }
            else state
        }
    }

    fun onTimestampChange(value: Long?) {
        val currentState = _uiState.value as? TaskUiState.Success ?: return
        if (value == currentState.dueTimestamp) return

        _uiState.update { state ->
            if (state is TaskUiState.Success) state.copy(dueTimestamp = value)
            else state
        }
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
        if (currentState.title.isBlank() && currentState.details.isNullOrBlank()) return@withLock

        val currentTask = currentState.toTask()
        val previousTask = lastSavedTask

        val isUnchanged = previousTask != null &&
            currentTask.title == previousTask.title &&
            currentTask.details == previousTask.details &&
            currentTask.dueTimestamp == previousTask.dueTimestamp &&
            currentTask.isCompleted == previousTask.isCompleted

        if (isUnchanged) return@withLock

        val now = System.currentTimeMillis()
        val taskToSave = currentTask.copy(modifiedAtTimestamp = now)
        val savedId = if (currentState.id == null || currentState.id == 0L) {
            val newId = taskRepository.insert(taskToSave)
            _uiState.update {
                (it as? TaskUiState.Success)?.copy(id = newId, modifiedAtTimestamp = now) ?: it
            }
            newId
        } else {
            taskRepository.update(taskToSave)
            _uiState.update {
                (it as? TaskUiState.Success)?.copy(modifiedAtTimestamp = now) ?: it
            }
            currentState.id
        }

        lastSavedTask = taskToSave.copy(id = savedId)

        val reminderChanged = previousTask == null ||
            previousTask.dueTimestamp != currentTask.dueTimestamp ||
            previousTask.title != currentTask.title ||
            previousTask.details != currentTask.details

        if (reminderChanged) {
            val dueTimestamp = currentTask.dueTimestamp
            if (dueTimestamp != null && dueTimestamp > Clock.System.now().toEpochMilliseconds()) {
                val notificationTitle = currentTask.title.ifBlank { currentTask.details.orEmpty() }
                val notificationDetails = if (currentTask.title.isNotBlank()) currentTask.details
                else null

                scheduleNotification(
                    taskId = savedId,
                    title = notificationTitle,
                    details = notificationDetails,
                    triggerAtMillis = dueTimestamp
                )
            } else if (dueTimestamp == null && previousTask?.dueTimestamp != null) {
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
        createdAtTimestamp = createdAtTimestamp,
        modifiedAtTimestamp = modifiedAtTimestamp,
        dueTimestamp = dueTimestamp,
        isCompleted = isDone
    )
}