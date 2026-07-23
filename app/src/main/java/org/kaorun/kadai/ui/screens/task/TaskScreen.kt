@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package org.kaorun.kadai.ui.screens.task

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ButtonGroupScope
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.calendar_month
import org.kaorun.kadai.ui.icons.close
import org.kaorun.kadai.ui.icons.schedule
import org.kaorun.kadai.ui.screens.task.components.ExtendedFloatingActionButton
import org.kaorun.kadai.ui.screens.task.components.TextField
import org.kaorun.kadai.ui.screens.task.components.TopAppBar
import org.kaorun.kadai.ui.theme.KadaiTheme
import org.kaorun.kadai.ui.utils.combineDateAndTime
import org.kaorun.kadai.ui.utils.toFormattedDate
import org.kaorun.kadai.ui.utils.toFormattedTime
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@Composable
fun TaskScreen(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TaskScreenContent(
        title = uiState.title,
        details = uiState.details,
        timestamp = uiState.timestamp,
        isDone = uiState.isDone,
        modifier = modifier,
        onTitleChange = viewModel::onTitleChange,
        onDetailsChange = viewModel::onDetailsChange,
        onTimestampChange = viewModel::onTimestampChange,
        onDoneChange = viewModel::onDoneChange,
        onDelete = viewModel::delete,
        onClose = onBack
    )
}

@Composable
fun TaskScreenContent(
    title: String,
    details: String?,
    timestamp: Long?,
    isDone: Boolean,
    modifier: Modifier = Modifier,
    onTitleChange: (String) -> Unit,
    onDetailsChange: (String) -> Unit,
    onTimestampChange: (Long?) -> Unit,
    onDoneChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val dateTime = remember(timestamp) {
        timestamp?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()) }
    }
    val timePickerState = rememberTimePickerState(
        initialHour = dateTime?.hour ?: LocalTime.now().plusHours(1).hour,
        initialMinute = dateTime?.minute ?: 0
    )
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = timestamp ?: Instant.now().toEpochMilli()
    )
    val isDateConfirmEnabled by remember {
        derivedStateOf {
            datePickerState.selectedDateMillis != null
        }
    }
    var isDialogTimeVisible by rememberSaveable { mutableStateOf(false) }
    var isDialogDateVisible by rememberSaveable { mutableStateOf(false) }
    var isTimeChecked by rememberSaveable(timestamp) {
        mutableStateOf(timestamp != null)
    }
    var isDateChecked by rememberSaveable(timestamp) {
        mutableStateOf(
            timestamp?.let {
                val date = Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                val today = LocalDate.now(ZoneId.systemDefault())
                date != today
            } ?: false
        )
    }
    var timePickerMode by retain { mutableStateOf(TimePickerDisplayMode.Picker) }


    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onClose,
                onDelete = onDelete
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onDoneChange(!isDone) },
                isDone = isDone,
                modifier = Modifier.imePadding()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .padding(bottom = 88.dp)
            ) {
                TextField(
                    text = title,
                    sampleText = stringResource(R.string.title),
                    textStyle = MaterialTheme.typography.headlineMedium,
                    onValueChange = onTitleChange,
                    modifier = modifier
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    text = details?: "",
                    sampleText = stringResource(R.string.details),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    onValueChange = onDetailsChange
                )

                val dateString = timestamp?.toFormattedDate(context)
                    ?: stringResource(R.string.task_date)
                val timeString = timestamp?.toFormattedTime(context)
                    ?: stringResource(R.string.task_time)

                ButtonGroup(
                    overflowIndicator = { menuState ->
                        ButtonGroupDefaults.OverflowIndicator(menuState = menuState)
                    },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    buttonGroupToggleItem(
                        onClick = { isDialogTimeVisible = true },
                        imageVector = schedule,
                        text = timeString,
                        isChecked = isTimeChecked
                    )

                    if (isTimeChecked) {
                        buttonGroupIconItem(
                            onClick = { onTimestampChange(null) }
                        )
                    }

                    timestamp?.let {
                        buttonGroupToggleItem(
                            onClick = { isDialogDateVisible = true },
                            imageVector = calendar_month,
                            text = dateString,
                            isChecked = isDateChecked
                        )
                    }

                    if (isDateChecked) {
                        buttonGroupIconItem(
                            onClick = {
                                onTimestampChange(
                                    combineDateAndTime(
                                        dateMillis = null,
                                        timeState = timePickerState
                                    )
                                )
                            }
                        )
                    }
                }
            }

            if (isDialogTimeVisible) {
                TimePickerDialog(
                    onDismissRequest = { isDialogTimeVisible = false },
                    confirmButton = {
                        TextButtonDialog(
                            onClick = {
                                val currentTimestamp = combineDateAndTime(
                                    dateMillis = datePickerState.selectedDateMillis ?: timestamp,
                                    timeState = timePickerState
                                )
                                onTimestampChange(currentTimestamp)
                                isTimeChecked = true
                                isDialogTimeVisible = false
                            },
                            text = stringResource(R.string.ok)
                        )
                    },
                    title = {
                        TimePickerDialogDefaults.Title(TimePickerDisplayMode.Picker)
                    },
                    dismissButton = {
                        TextButtonDialog(
                            onClick = { isDialogTimeVisible = false },
                            text = stringResource(R.string.cancel)
                        )
                    },
                    modeToggleButton = {
                        if (LocalWindowInfo.current.containerSize.height.dp
                            > TimePickerDialogDefaults.MinHeightForTimePicker) {
                            TimePickerDialogDefaults.DisplayModeToggle(
                                onDisplayModeChange = {
                                    timePickerMode =
                                        if (timePickerMode == TimePickerDisplayMode.Picker) {
                                            TimePickerDisplayMode.Input
                                        } else {
                                            TimePickerDisplayMode.Picker
                                        }
                                },
                                displayMode = timePickerMode
                            )
                        }
                    }
                ) {
                    if (timePickerMode == TimePickerDisplayMode.Picker
                        && LocalWindowInfo.current.containerSize.height.dp
                        > TimePickerDialogDefaults.MinHeightForTimePicker ) {
                        TimePicker(state = timePickerState)
                    } else {
                        TimeInput(state = timePickerState)
                    }
                }
            }

            if (isDialogDateVisible) {
                LaunchedEffect(Unit) {
                    datePickerState.selectedDateMillis = timestamp ?: Instant.now().toEpochMilli()
                }

                DatePickerDialog(
                    onDismissRequest = { isDialogDateVisible = false },
                    confirmButton = {
                        TextButtonDialog(
                            onClick = {
                                val currentTimestamp = combineDateAndTime(
                                    dateMillis = datePickerState.selectedDateMillis,
                                    timeState = timePickerState
                                )
                                onTimestampChange(currentTimestamp)
                                val pickedDate = Instant.ofEpochMilli(currentTimestamp)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                val today = LocalDate.now(ZoneId.systemDefault())
                                isDateChecked = pickedDate != today
                                isDialogDateVisible = false
                            },
                            text = stringResource(R.string.ok),
                            isEnabled = isDateConfirmEnabled
                        )
                    },
                    dismissButton = {
                        TextButtonDialog(
                            onClick = { isDialogDateVisible = false },
                            text = stringResource(R.string.cancel)
                        )
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            }
        }
    }
}



private fun ButtonGroupScope.buttonGroupToggleItem(
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String,
    isChecked: Boolean = false
) {
    customItem(
        buttonGroupContent = {
            val interactionSource = remember { MutableInteractionSource() }
            val textMeasurer = rememberTextMeasurer()
            val density = LocalDensity.current
            val textWidth = remember(text) {
                with(density) {
                    textMeasurer.measure(text).size.width.toDp()
                }
            }
            ToggleButton(
                checked = isChecked,
                onCheckedChange = { onClick() },
                interactionSource = interactionSource,
                shapes = ToggleButtonDefaults.shapes(),
                colors = ToggleButtonDefaults.tonalToggleButtonColors(),
                contentPadding = ButtonDefaults.ExtraSmallContentPadding,
                modifier = Modifier
                    .animateWidth(interactionSource)
                    .widthIn(min = (textWidth + 32.dp).coerceAtLeast(minimumValue = 80.dp))
                    .heightIn(min = ButtonDefaults.ExtraSmallContainerHeight)
            ) {
                if (!isChecked) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        modifier = Modifier
                            .height(20.dp)
                            .padding(end = 4.dp)
                    )
                }

                Text(
                    text = text,
                    maxLines = 1,
                    style = ButtonDefaults.textStyleFor(
                        buttonHeight = ButtonDefaults.ExtraSmallContainerHeight
                    )
                )
            }
        },
        menuContent = { }
    )
}

private fun ButtonGroupScope.buttonGroupIconItem(
    onClick: () -> Unit
) {
    customItem(
        buttonGroupContent = {
            val interactionSource = remember { MutableInteractionSource() }
            IconButton(
                onClick = onClick,
                shapes = IconButtonDefaults.shapes(),
                colors = IconButtonDefaults.filledTonalIconButtonColors(),
                interactionSource = interactionSource,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .animateWidth(interactionSource)
                    .height(ButtonDefaults.ExtraSmallContainerHeight)
                    .width(32.dp)
            ) {
                Icon(
                    imageVector = close,
                    contentDescription = stringResource(R.string.close),
                    modifier = Modifier.height(20.dp)
                )
            }
        },
        menuContent = { }
    )
}

@Composable
private fun TextButtonDialog(
    onClick: () -> Unit,
    text: String,
    isEnabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        enabled = isEnabled
    ) {
        Text(text)
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskScreenContentPreview() {
    KadaiTheme {
        TaskScreenContent(
            title = "Buy bread for Teto",
            details = "Bread for 31yo unemployed fatass",
            timestamp = null,
            isDone = false,
            onTitleChange = { },
            onDetailsChange = { },
            onTimestampChange = { },
            onDoneChange = { },
            onDelete = { },
            onClose = { }
        )
    }
}