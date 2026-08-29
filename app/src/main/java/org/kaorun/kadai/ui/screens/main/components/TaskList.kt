package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.ui.icons.task_alt
import org.kaorun.kadai.ui.screens.main.utils.animatedStrikethrough
import org.kaorun.kadai.ui.utils.toFormattedDate
import org.kaorun.kadai.ui.utils.toFormattedTime
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TaskList(
    tasks: List<Task>,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues,
    showPermissionCard: Boolean,
    onClick: (Task) -> Unit,
    onCheck: (Task, Boolean) -> Unit,
    onPermissionCardClick: () -> Unit,
    onPermissionCardCloseClick: () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current
    val cornerSize = 20.dp
    val innerCornerSize = 4.dp
    val colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceBright)

    if (tasks.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TaskListEmpty(
                icon = task_alt,
                title = stringResource(R.string.task_list_empty)
            )
        }
    } else {
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(
                start = contentPadding.calculateStartPadding(layoutDirection),
                top = contentPadding.calculateTopPadding(),
                end = contentPadding.calculateEndPadding(layoutDirection),
                bottom = contentPadding.calculateBottomPadding() + 64.dp
            ),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
        ) {
            if (showPermissionCard) {
                item(key = "permission_missing_card", contentType = "permission_card") {
                    PermissionMissingCard(
                        onClick = onPermissionCardClick,
                        onCloseClick = onPermissionCardCloseClick
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            itemsIndexed(
                items = tasks,
                key = { _, task -> task.id },
                contentType = { _, _ -> "task_item" }
            ) { index, task ->
                var isChecked by remember(task.id, task.isCompleted) {
                    mutableStateOf(task.isCompleted)
                }
                val coroutineScope = rememberCoroutineScope()
                val strikeProgress by animateFloatAsState(
                    targetValue = if (isChecked) 1f else 0f,
                    animationSpec = tween(
                        durationMillis = 150,
                        easing = FastOutSlowInEasing
                    )
                )

                val shape = remember(index, tasks.size) {
                    when {
                        tasks.size == 1 -> RoundedCornerShape(cornerSize)
                        index == 0 -> RoundedCornerShape(
                            topStart = cornerSize,
                            topEnd = cornerSize,
                            bottomStart = innerCornerSize,
                            bottomEnd = innerCornerSize
                        )

                        index == tasks.lastIndex -> RoundedCornerShape(
                            topStart = innerCornerSize,
                            topEnd = innerCornerSize,
                            bottomStart = cornerSize,
                            bottomEnd = cornerSize
                        )

                        else -> RoundedCornerShape(innerCornerSize)
                    }
                }

                SegmentedListItem(
                    onClick = { onClick(task) },
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = tasks.size
                    ),
                    modifier = Modifier
                        .clip(shape)
                        .animateItem(),
                    colors = colors,
                    leadingContent = {
                        RoundedCheckbox(
                            checked = isChecked,
                            onValueChange = { checked ->
                                isChecked = checked
                                coroutineScope.launch {
                                    delay(200.milliseconds)
                                    onCheck(task, checked)
                                }
                            }
                        )
                    },
                    content = {
                        Text(
                            text = task.title,
                            maxLines = 1,
                            modifier = Modifier.animatedStrikethrough(
                                progress = strikeProgress,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    supportingContent = task.details?.takeIf { it.isNotBlank() }?.let { details ->
                        {
                            Text(
                                text = details,
                                maxLines = 2,
                                modifier = Modifier.animatedStrikethrough(
                                    progress = strikeProgress,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    },
                    trailingContent = task.dueTimestamp?.let { timestamp ->
                        {
                            val context = LocalContext.current
                            val date = timestamp.toFormattedDate(context)
                            val time = timestamp.toFormattedTime(context)
                            Text(
                                text = "$date, $time",
                                modifier = Modifier.animatedStrikethrough(
                                    progress = strikeProgress,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically
                )
            }
        }
    }
}