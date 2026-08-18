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
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()
    private enum class LoadState { NOT_LOADED, LOADING, LOADED }
    private var loadState = LoadState.NOT_LOADED
    private var isDeleted = false

    init {
        _uiState
            .distinctUntilChanged { old, new ->
                old.title == new.title &&
                old.details == new.details &&
                old.timestamp == new.timestamp &&
                old.isDone == new.isDone
            }
            .debounce(400)
            .onEach { if (loadState != LoadState.LOADING && !isDeleted) save() }
            .launchIn(viewModelScope)
    }

    fun load(taskId: Long) {
        if (loadState != LoadState.NOT_LOADED) return
        if (taskId == 0L) {
            loadState = LoadState.LOADED
            return
        }

        loadState = LoadState.LOADING
        viewModelScope.launch {
            taskRepository.getTaskById(taskId)?.let {
                _uiState.value = TaskUiState(
                    id = it.id,
                    title = it.title,
                    details = it.details,
                    timestamp = it.timestamp,
                    isDone = it.isCompleted
                )
            }
            loadState = LoadState.LOADED
        }
    }


    fun onBack(navigateBack: () -> Unit) {
        if (loadState != LoadState.LOADING && !isDeleted) {
            viewModelScope.launch {
                save()
                navigateBack()
            }
        } else {
            navigateBack()
        }
    }

    fun onTitleChange(value: String) { _uiState.update { it.copy(title = value) } }
    fun onDetailsChange(value: String) { _uiState.update { it.copy(details = value) } }
    fun onDoneChange(value: Boolean) { _uiState.update { it.copy(isDone = value) } }
    fun onTimestampChange(value: Long?) {
        if (value == _uiState.value.timestamp) return

        _uiState.update { it.copy(timestamp = value) }
        viewModelScope.launch { save() }
    }
    fun onDelete() {
        isDeleted = true
        viewModelScope.launch {
            mutex.withLock {
                with(_uiState.value) {
                    if (id != 0L) {
                        cancelNotification(taskId = id)
                        taskRepository.delete(task = toTask())
                    }
                }
            }
        }
    }

    private suspend fun save() = mutex.withLock {
        with(_uiState.value) {
            if (title.isBlank()) {
                if (id != 0L) {
                    cancelNotification(taskId = id)
                    taskRepository.delete(task = toTask())
                    _uiState.update { it.copy(id = 0L) }
                }
                return@withLock
            }

            val id = if (id == 0L) {
                taskRepository.insert(task = toTask()).also { newId ->
                    _uiState.update { it.copy(id = newId) }
                }
            } else {
                taskRepository.update(task = toTask())
                id
            }

            if (timestamp != null && timestamp > Clock.System.now().toEpochMilliseconds()) {
                scheduleNotification(
                    taskId = id,
                    title = title,
                    details = details,
                    triggerAtMillis = timestamp
                )
            } else {
                cancelNotification(taskId = id)
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
        android.util.Log.d("TaskVM", "cancelNotification taskId=$taskId existing=${existing?.id}")
        existing?.let { alarmScheduler.cancel(it) }
        scheduledNotificationRepository.deleteByTaskId(taskId)
    }

    private fun TaskUiState.toTask() = Task(
        id = id,
        title = title,
        details = details,
        timestamp = timestamp,
        isCompleted = isDone
    )
}