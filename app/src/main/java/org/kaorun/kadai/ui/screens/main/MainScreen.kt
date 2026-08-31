@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class
)

package org.kaorun.kadai.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kaorun.kadai.R.string
import org.kaorun.kadai.data.entity.Task
import org.kaorun.kadai.data.model.TaskSortConfig
import org.kaorun.kadai.data.model.TaskSortField
import org.kaorun.kadai.ui.icons.add
import org.kaorun.kadai.ui.icons.done_all
import org.kaorun.kadai.ui.icons.filled.list_filled
import org.kaorun.kadai.ui.screens.main.MainUiState.Loading
import org.kaorun.kadai.ui.screens.main.MainUiState.Success
import org.kaorun.kadai.ui.screens.main.MainViewModel.TaskSnackbarMessage
import org.kaorun.kadai.ui.screens.main.components.FloatingToolbar
import org.kaorun.kadai.ui.screens.main.components.ModalWideNavigationRail
import org.kaorun.kadai.ui.screens.main.components.TaskList
import org.kaorun.kadai.ui.screens.main.components.TopAppBar
import org.kaorun.kadai.ui.screens.main.utils.UndoScrollHandler
import org.kaorun.kadai.ui.screens.permission.utils.rememberPermissionsGranted

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    selectedTaskId: Long? = null,
    onNavigateToTask: (Long?) -> Unit,
    onNavigateToSettings: () -> Unit,
    onPermissionCardClick: () -> Unit
) {
    val uiState by viewModel.mainUiState.collectAsStateWithLifecycle()
    val sortConfig by viewModel.sortConfig.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val isPermissionCardDismissed by viewModel.isPermissionCardDismissed.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    MainScreenContent(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        sortConfig = sortConfig,
        snackbarMessage = snackbarMessage,
        snackbarHostState = snackbarHostState,
        selectedTaskId = selectedTaskId,
        onNavigateToTask = onNavigateToTask,
        onNavigateToSettings = onNavigateToSettings,
        onSortFieldSelected = viewModel::onSortFieldSelected,
        onTaskCompletionToggled = viewModel::onTaskCompletionToggled,
        onUndoTaskCompletion = viewModel::onUndoTaskCompletion,
        onSnackbarMessageDismissed = viewModel::onSnackbarMessageDismissed,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        recentSearches = recentSearches,
        onSaveRecentSearch = viewModel::onSaveRecentSearch,
        onDeleteRecentSearch = viewModel::onDeleteRecentSearch,
        onRecentSearchFilterChange = viewModel::onRecentSearchFilterChanged,
        permissionCardDismissed = isPermissionCardDismissed,
        onPermissionCardClick = onPermissionCardClick,
        onPermissionDismissed = viewModel::onPermissionDismissed
    )
}

@Composable
fun MainScreenContent(
    uiState: MainUiState,
    sortConfig: TaskSortConfig,
    snackbarMessage: TaskSnackbarMessage?,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    selectedTaskId: Long? = null,
    onSortFieldSelected: (TaskSortField) -> Unit,
    onNavigateToTask: (Long?) -> Unit,
    onNavigateToSettings: () -> Unit,
    onTaskCompletionToggled: (Task, Boolean) -> Unit,
    onUndoTaskCompletion: (Task, Boolean) -> Unit,
    onSnackbarMessageDismissed: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    recentSearches: List<String> = emptyList(),
    onSaveRecentSearch: (String) -> Unit = {},
    onDeleteRecentSearch: (String) -> Unit = {},
    onRecentSearchFilterChange: (String) -> Unit = {},
    permissionCardDismissed: Boolean,
    onPermissionCardClick: () -> Unit,
    onPermissionDismissed: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val permissionsGranted = rememberPermissionsGranted()
    val navigationRailState = rememberWideNavigationRailState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val pendingListState = rememberLazyListState()
    val completedListState = rememberLazyListState()

    val permissionCardVisible = !permissionsGranted && !permissionCardDismissed
    val currentUiState by rememberUpdatedState(uiState)

    val undoScrollHandler = remember(
        pagerState,
        pendingListState,
        completedListState,
        permissionCardVisible
    ) {
        UndoScrollHandler(
            scope = scope,
            pagerState = pagerState,
            pendingListState = pendingListState,
            completedListState = completedListState,
            permissionCardVisible = permissionCardVisible,
            currentTasks = {
                when (val state = currentUiState) {
                    is Success -> state.pendingTasks + state.completedTasks
                    else -> emptyList()
                }
            }
        )
    }

    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val isWideScreen =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    Box(modifier = modifier.fillMaxSize()) {
        ModalWideNavigationRail(
            navigationRailState = navigationRailState,
            onNavigateToSettings = onNavigateToSettings
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = if (isWideScreen) {
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Start + WindowInsetsSides.Bottom
                )
            } else {
                ScaffoldDefaults.contentWindowInsets
            },
            topBar = {
                TopAppBar(
                    navigationRailState = navigationRailState,
                    sortConfig = sortConfig,
                    onSortByClick = onSortFieldSelected,
                    onSearch = onSearchQueryChange,
                    recentSearches = recentSearches,
                    onSaveRecentSearch = onSaveRecentSearch,
                    onDeleteRecentSearch = onDeleteRecentSearch,
                    onRecentSearchFilterChange = onRecentSearchFilterChange
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                FloatingBar(
                    pagerState = pagerState,
                    scope = scope,
                    onNavigateToTask = onNavigateToTask
                )
            },
            containerColor = colorScheme.surfaceContainer
        ) { innerPadding ->
            val layoutDirection = LocalLayoutDirection.current
            val listContentPadding = PaddingValues(
                start = 16.dp + innerPadding.calculateStartPadding(layoutDirection),
                top = innerPadding.calculateTopPadding() + 16.dp,
                end = 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 88.dp
            )

            val handleTaskClick = remember(onNavigateToTask) {
                { task: Task -> onNavigateToTask(task.id) }
            }

            when (uiState) {
                is Loading -> Unit
                is Success -> {
                    val effectiveSelectedTaskId = if (isWideScreen) selectedTaskId else null

                    HorizontalPager(
                        state = pagerState,
                        beyondViewportPageCount = 1,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        when (page) {
                            0 -> TaskList(
                                tasks = uiState.pendingTasks,
                                state = pendingListState,
                                contentPadding = listContentPadding,
                                showPermissionCard = permissionCardVisible,
                                selectedTaskId = effectiveSelectedTaskId,
                                onClick = handleTaskClick,
                                onCheck = onTaskCompletionToggled,
                                onPermissionCardClick = onPermissionCardClick,
                                onPermissionCardCloseClick = onPermissionDismissed
                            )

                            1 -> TaskList(
                                tasks = uiState.completedTasks,
                                state = completedListState,
                                contentPadding = listContentPadding,
                                showPermissionCard = permissionCardVisible,
                                selectedTaskId = effectiveSelectedTaskId,
                                onClick = handleTaskClick,
                                onCheck = onTaskCompletionToggled,
                                onPermissionCardClick = onPermissionCardClick,
                                onPermissionCardCloseClick = onPermissionDismissed
                            )
                        }
                    }
                }
            }
        }
    }

    snackbarMessage?.let { currentMessage ->
        val message = stringResource(id = currentMessage.messageResId)
        val undoMessage = stringResource(string.undo)

        LaunchedEffect(currentMessage.id) {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = undoMessage,
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
                val restoredTask = currentMessage.task
                val targetState = currentMessage.previousCompletedState

                onUndoTaskCompletion(restoredTask, targetState)
                undoScrollHandler.handle(restoredTask, targetState)
            }

            onSnackbarMessageDismissed()
        }
    }
}

@Composable
private fun FloatingBar(
    pagerState: PagerState,
    scope: CoroutineScope,
    onNavigateToTask: (Long?) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val pendingText = stringResource(string.pending)
        val completedText = stringResource(string.completed)
        val toolbarItems = remember(pendingText, completedText) {
            mapOf(
                list_filled to pendingText,
                done_all to completedText
            )
        }

        FloatingToolbar(
            onItemSelected = { index ->
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            items = toolbarItems,
            selectedIndex = pagerState.targetPage
        )

        val addTaskDescription = stringResource(string.add_task)
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
                containerColor = colorScheme.primaryContainer,
                contentColor = colorScheme.onPrimaryContainer
            ) {
                Icon(imageVector = add, contentDescription = addTaskDescription)
            }
        }
    }
}