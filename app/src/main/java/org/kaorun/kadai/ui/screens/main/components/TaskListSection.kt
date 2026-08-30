package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.data.entity.Task
import org.kaorun.kadai.ui.icons.keyboard_arrow_down
import org.kaorun.kadai.ui.screens.main.utils.animatedStrikethrough
import org.kaorun.kadai.ui.utils.toFormattedDate
import org.kaorun.kadai.ui.utils.toFormattedTime
import kotlin.time.Duration.Companion.milliseconds

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
                    var isChecked by remember(task.id, task.isCompleted) {
                        mutableStateOf(task.isCompleted)
                    }
                    val coroutineScope = rememberCoroutineScope()
                    val strikeProgress by animateFloatAsState(
                        targetValue = if (isChecked) 1f else 0f,
                        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
                        label = "strikethrough_progress"
                    )

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
}