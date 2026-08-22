package org.kaorun.kadai.ui.screens.main.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kaorun.kadai.data.Task

class UndoScrollHandler(
    private val scope: CoroutineScope,
    private val pagerState: PagerState,
    private val pendingListState: LazyListState,
    private val completedListState: LazyListState,
    private val permissionCardVisible: Boolean,
    private val currentTasks: () -> List<Task>
) {
    fun handle(restoredTask: Task, targetState: Boolean) {
        val targetPage = if (targetState) 1 else 0
        val targetListState = if (targetState) completedListState else pendingListState

        scope.launch {
            if (pagerState.currentPage != targetPage) {
                pagerState.animateScrollToPage(targetPage)
            }

            snapshotFlow { currentTasks() }
                .map { tasksList ->
                    val taskIndex = tasksList
                        .filter { it.isCompleted == targetState }
                        .indexOfFirst { it.id == restoredTask.id }

                    if (taskIndex == -1) -1
                    else {
                        val headerOffset = if (permissionCardVisible && targetPage == 0) 1 else 0
                        taskIndex + headerOffset
                    }
                }
                .first { index -> index != -1 }
                .let { index ->
                    targetListState.smoothScrollToItem(index = index)
                }
        }
    }
}