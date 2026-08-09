package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.R
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.ui.icons.keyboard_arrow_down
import org.kaorun.kadai.ui.utils.toFormattedDate
import org.kaorun.kadai.ui.utils.toFormattedTime

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskList(
    tasks: List<Task>,
    contentPadding: PaddingValues,
    onClick: (Task) -> Unit,
    onCheck: (Task, Boolean) -> Unit
) {
    val cornerSize = 20.dp
    val innerCornerSize = 4.dp
    val colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceBright)
    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        itemsIndexed(
            items = tasks,
            key = {_, task -> task.id }
        ) { index, task ->
            val shape = remember(index, tasks.size) {
                if (tasks.size == 1) RoundedCornerShape(cornerSize)
                when (index) {
                    0 -> RoundedCornerShape(
                        topStart = cornerSize,
                        topEnd = cornerSize,
                        bottomStart = innerCornerSize,
                        bottomEnd = innerCornerSize
                    )
                    tasks.lastIndex -> RoundedCornerShape(
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
                    index = index + 1,
                    count = tasks.size + 1,
                ),
                modifier = Modifier.clip(shape),
                colors = colors,
                leadingContent = {
                    RoundedCheckbox(
                        checked = task.isDone,
                        onValueChange = { checked -> onCheck(task, checked) }
                    )
                },
                content = {
                    Text(
                        text = task.title,
                        maxLines = 1,
                        textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                    )
                },
                supportingContent = {
                    task.details?.let {
                        Text(
                            text = it,
                            maxLines = 2,
                            textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                        )
                    }
                },
                trailingContent = {
                    task.timestamp?.let {
                        val context = LocalContext.current
                        val date = it.toFormattedDate(context)
                        val time = it.toFormattedTime(context)
                        Text(
                            text = "$date, $time",
                            textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                        )
                    }
                },
                verticalAlignment = Alignment.CenterVertically
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskListSection(
    tasks: List<Task>,
    title: String,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onClick: (Task) -> Unit,
    onCheck: (Task, Boolean) -> Unit
) {
    val itemCount = 1 + if (expanded) tasks.size else 0
    val cornerSize = 20.dp
    val innerCornerSize = 4.dp
    val colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceBright)

    Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
        val headerShape = remember(itemCount) {
            if (itemCount == 1) RoundedCornerShape(cornerSize)
            else RoundedCornerShape(
                topStart = cornerSize, topEnd = cornerSize,
                bottomStart = innerCornerSize, bottomEnd = innerCornerSize
            )
        }
        val rotation by animateFloatAsState(
            targetValue = if (expanded) 180f else 0f,
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
            label = "chevron_rotation"
        )

        SegmentedListItem(
            onClick = { onExpandChange(!expanded) },
            shapes = ListItemDefaults.segmentedShapes(index = 0, count = itemCount),
            modifier = Modifier.clip(headerShape),
            colors = colors,
            trailingContent = {
                Icon(
                    imageVector = keyboard_arrow_down,
                    contentDescription = if (expanded) stringResource(R.string.collapse)
                    else stringResource(R.string.expand),
                    modifier = Modifier.rotate(rotation)
                )
            }
        ) {
            Text("$title (${tasks.size})")
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(MaterialTheme.motionScheme.fastSpatialSpec()),
            exit = shrinkVertically(MaterialTheme.motionScheme.fastSpatialSpec()),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
                tasks.forEachIndexed { index, task ->
                    val shape = remember(index, tasks.size) {
                        if (index == tasks.lastIndex)
                            RoundedCornerShape(
                                topStart = innerCornerSize, topEnd = innerCornerSize,
                                bottomStart = cornerSize, bottomEnd = cornerSize
                            )
                        else RoundedCornerShape(innerCornerSize)
                    }

                    SegmentedListItem(
                        onClick = { onClick(task) },
                        shapes = ListItemDefaults.segmentedShapes(
                            index = index + 1,
                            count = tasks.size + 1,
                        ),
                        modifier = Modifier.clip(shape),
                        colors = colors,
                        leadingContent = {
                            RoundedCheckbox(
                                checked = task.isDone,
                                onValueChange = { checked -> onCheck(task, checked) }
                            )
                        },
                        content = {
                            Text(
                                text = task.title,
                                maxLines = 1,
                                textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                            )
                        },
                        supportingContent = {
                            task.details?.let {
                                Text(
                                    text = it,
                                    maxLines = 2,
                                    textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                                )
                            }
                        },
                        trailingContent = {
                            task.timestamp?.let {
                                val context = LocalContext.current
                                val date = it.toFormattedDate(context)
                                val time = it.toFormattedTime(context)
                                Text(
                                    text = "$date, $time",
                                    textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}