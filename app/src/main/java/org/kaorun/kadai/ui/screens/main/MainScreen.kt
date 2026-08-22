@file:OptIn(ExperimentalMaterial3Api::class)

package org.kaorun.kadai.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.ui.icons.add_task
import org.kaorun.kadai.ui.icons.done_all
import org.kaorun.kadai.ui.icons.filled.list_filled
import org.kaorun.kadai.ui.screens.main.components.FloatingToolbar
import org.kaorun.kadai.ui.screens.main.components.ModalWideNavigationRail
import org.kaorun.kadai.ui.screens.main.components.TaskList
import org.kaorun.kadai.ui.screens.main.components.TopAppBar
import org.kaorun.kadai.ui.screens.permission.utils.rememberPermissionsGranted
import org.kaorun.kadai.ui.theme.KadaiTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToTask: (Long?) -> Unit,
    onPermissionCardClick: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    MainScreenContent(
        tasks = tasks,
        onNavigateToTask = onNavigateToTask,
        onCheck = viewModel::onTaskCheck,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onPermissionCardClick = onPermissionCardClick,
    )
}

@Composable
fun MainScreenContent(
    tasks: List<Task>,
    onNavigateToTask: (Long?) -> Unit,
    onCheck: (Task, Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onPermissionCardClick: () -> Unit
) {
    val navigationRailState = rememberWideNavigationRailState()
    val scope = rememberCoroutineScope()
    val permissionsGranted = rememberPermissionsGranted()
    val pagerState = rememberPagerState(pageCount = { 2 })

    ModalWideNavigationRail(navigationRailState = navigationRailState)

    Scaffold(
        topBar = {
            TopAppBar(
                navigationRailState = navigationRailState,
                onSearch = { query ->
                   onSearchQueryChange(query)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingToolbar(
                    onItemSelected = { index ->
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    items = mapOf(
                        list_filled to stringResource(R.string.pending),
                        done_all to stringResource(R.string.completed)
                    ),
                    selectedIndex = pagerState.currentPage
                )

                val addTaskDescription = stringResource(R.string.add_task)
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                        TooltipAnchorPosition.Above
                    ),
                    tooltip = {
                        PlainTooltip(
                            modifier = Modifier.semantics {
                                liveRegion = LiveRegionMode.Assertive
                                paneTitle = addTaskDescription
                            }
                        ) {
                            Text(addTaskDescription)
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    FloatingActionButton(
                        onClick = { onNavigateToTask(null) },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Icon(imageVector = add_task, contentDescription = null)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        val listContentPadding = PaddingValues(
            start = 16.dp +
                    innerPadding.calculateStartPadding(LocalLayoutDirection.current),
            top = innerPadding.calculateTopPadding() + 16.dp,
            end = 16.dp +
                    innerPadding.calculateEndPadding(LocalLayoutDirection.current),
            bottom = innerPadding.calculateBottomPadding() + 16.dp
        )
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> TaskList(
                    tasks = remember(tasks) { tasks.filter { !it.isCompleted } },
                    showPermissionCard = !permissionsGranted,
                    contentPadding = listContentPadding,
                    onClick = { task -> onNavigateToTask(task.id) },
                    onCheck = onCheck,
                    onPermissionCardClick = onPermissionCardClick
                )
                1 -> TaskList(
                    tasks = remember(tasks) { tasks.filter { it.isCompleted } },
                    showPermissionCard = !permissionsGranted,
                    contentPadding = listContentPadding,
                    onClick = { task -> onNavigateToTask(task.id) },
                    onCheck = onCheck,
                    onPermissionCardClick = onPermissionCardClick
                )
            }
        }
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
            onPermissionCardClick = { },
            onCheck = { _, _ -> },
            onSearchQueryChange = {_ -> }
        )
    }
}