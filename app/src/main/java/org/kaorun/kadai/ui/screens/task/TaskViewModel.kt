package org.kaorun.kadai.ui.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.data.TaskRepository
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private var isLoading = true

    private var isLoaded = false
    private var isDeleted = false

    init {
        _uiState
            .debounce(400)
            .distinctUntilChanged { old, new ->
                old.title == new.title &&
                old.details == new.details &&
                old.timestamp == new.timestamp &&
                old.isDone == new.isDone
            }
            .onEach { if (!isLoading && !isDeleted) save(it) }
            .launchIn(viewModelScope)
    }

    fun onTitleChange(value: String) { _uiState.update { it.copy(title = value) } }
    fun onDetailsChange(value: String) { _uiState.update { it.copy(details = value) } }
    fun onTimestampChange(value: Long?) { _uiState.update { it.copy(timestamp = value) } }
    fun onDoneChange(value: Boolean) { _uiState.update { it.copy(isDone = value) } }

    fun load(taskId: Long) {
        if (isLoaded || taskId == 0L) return

        viewModelScope.launch {
            repository.getTaskById(taskId)?.let { task ->
                _uiState.value = TaskUiState(
                    id = task.id,
                    title = task.title,
                    details = task.details,
                    timestamp = task.timestamp,
                    isDone = task.isDone
                )
                isLoaded = true
            }
            isLoading = false
        }
    }

    private suspend fun save(state: TaskUiState) {
        if (state.title.isBlank()) {
            if (state.id != 0L) {
                withContext(Dispatchers.IO) { repository.delete(state.toTask()) }
                _uiState.update { it.copy(id = 0L) }
            }
            return
        }

        withContext(Dispatchers.IO) {
            if (state.id == 0L) {
                val newId = repository.insert(state.toTask())
                _uiState.update { it.copy(id = newId) }
            } else {
                repository.update(state.toTask())
            }
        }
    }

    fun delete() {
        isDeleted = true
        val state = _uiState.value
        if (state.id == 0L) return

        _uiState.update { TaskUiState() }

        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(state.toTask())
        }
    }

    override fun onCleared() {
        val state = _uiState.value
        if (state.title.isNotBlank() && !isDeleted) {
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                save(state)
            }
        }
    }

    private fun TaskUiState.toTask() = Task(
        id = id,
        title = title,
        details = details,
        timestamp = timestamp,
        isDone = isDone
    )
}