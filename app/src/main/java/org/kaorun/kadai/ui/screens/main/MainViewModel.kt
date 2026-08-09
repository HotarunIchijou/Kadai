package org.kaorun.kadai.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.data.TaskRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    val tasks: StateFlow<List<Task>> = repository.allTasks
        .combine(_searchQuery) { tasks, query ->
            if (query.isBlank()) tasks
            else tasks.filter { task ->
                task.title.contains(query, ignoreCase = true) ||
                task.details?.contains(query, ignoreCase = true) ?: false
            }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun onTaskCheck(task: Task, isDone: Boolean) {
        viewModelScope.launch {
            repository.update(task.copy(isDone = isDone))
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}