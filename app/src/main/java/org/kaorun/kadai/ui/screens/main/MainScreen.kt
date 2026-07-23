package org.kaorun.kadai.ui.screens.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.ui.screens.main.components.LargeFloatingActionButton
import org.kaorun.kadai.ui.screens.main.components.ModalWideNavigationRail
import org.kaorun.kadai.ui.screens.main.components.TaskListPending
import org.kaorun.kadai.ui.screens.main.components.TopAppBar
import org.kaorun.kadai.ui.theme.KadaiTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToTask: (Long?) -> Unit,
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    MainScreenContent(
        tasks = tasks,
        onNavigateToTask = onNavigateToTask,
        onCheck = viewModel::onTaskCheck,
    )
}

@Composable
fun MainScreenContent(
    tasks: List<Task>,
    onNavigateToTask: (Long?) -> Unit,
    onCheck: (Task, Boolean) -> Unit
) {
    val navigationRailState = rememberWideNavigationRailState()
    val scope = rememberCoroutineScope()

    ModalWideNavigationRail(
        navigationRailState = navigationRailState,
        scope = scope
   )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationRailState = navigationRailState,
                scope = scope
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { onNavigateToTask(null) }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { padding ->
        TaskListPending(
            padding = padding,
            tasks = tasks.filter { !it.isDone },
            onClick = { task -> onNavigateToTask(task.id) },
            onCheck = onCheck
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainScreenContentPreview() {
    val tasks = List(10) {
        Task(
            id = it.toLong(),
            title = "Task ${it + 1}",
            details = "Lorem ipsum dolor sit amet"
        )
    }

    KadaiTheme {
        MainScreenContent(
            tasks = tasks,
            onNavigateToTask = { },
            onCheck = { _, _ -> }
        )
    }
}