package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.ui.utils.toFormattedDate
import org.kaorun.kadai.ui.utils.toFormattedTime

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskListPending(
    padding: PaddingValues,
    tasks: List<Task>,
    onClick: (Task) -> Unit,
    onCheck: (Task, Boolean) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(true) }
    val itemCount = 1 + if (expanded) tasks.size else 0
    val cornerSize = 20.dp
    val innerCornerSize = 4.dp
    val horizontalPadding = 16.dp

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            top = padding.calculateTopPadding(),
            end = horizontalPadding,
            bottom = padding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            val headerShape = remember(itemCount) {
                if (itemCount == 1) RoundedCornerShape(cornerSize)
                else RoundedCornerShape(
                    topStart = cornerSize, topEnd = cornerSize,
                    bottomStart = innerCornerSize, bottomEnd = innerCornerSize
                )
            }
            SegmentedListItem(
                onClick = { expanded = !expanded },
                shapes = ListItemDefaults.segmentedShapes(index = 0, count = itemCount),
                modifier = Modifier
                    .animateItem()
                    .clip(headerShape),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceBright
                )
            ) {
                Text("Test")
            }
        }

        if (expanded) {
            itemsIndexed(items = tasks, key = { _, task -> task.id }) { index, task ->
                val shape = remember(index, tasks.size) {
                    when {
                        index == tasks.lastIndex -> RoundedCornerShape(
                            topStart = innerCornerSize, topEnd = innerCornerSize,
                            bottomStart = cornerSize, bottomEnd = cornerSize
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
                    modifier = Modifier
                        .animateItem()
                        .clip(shape),
                    leadingContent = {
                        RoundedCheckbox(
                            checked = task.isDone,
                            onValueChange = { checked ->
                                onCheck(task, checked)
                            }
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
                            val dateTime = "$date, $time"
                            Text(
                                text = dateTime,
                                textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                            )
                        }
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceBright
                    )
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}