@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package org.kaorun.kadai.ui.screens.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.calendar_month
import org.kaorun.kadai.ui.icons.schedule
import org.kaorun.kadai.ui.screens.task.components.DatePicker
import org.kaorun.kadai.ui.screens.task.components.ExtendedFloatingActionButton
import org.kaorun.kadai.ui.screens.task.components.TextField
import org.kaorun.kadai.ui.screens.task.components.TimePicker
import org.kaorun.kadai.ui.screens.task.components.TopAppBar
import org.kaorun.kadai.ui.screens.task.components.buttonGroupIconItem
import org.kaorun.kadai.ui.screens.task.components.buttonGroupToggleItem
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
        onDelete = viewModel::onDelete,
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
    val isTimeChecked = timestamp != null
    val isDateChecked = dateTime?.let {
        it.toLocalDate() != LocalDate.now(ZoneId.systemDefault())
    } ?: false
    var isDialogTimeVisible by rememberSaveable { mutableStateOf(false) }
    var isDialogDateVisible by rememberSaveable { mutableStateOf(false) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 88.dp)
                .padding(padding)
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
                text = details ?: "",
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
            TimePicker(
                onConfirm = { timePickerState ->
                    val updatedTimestamp = combineDateAndTime(
                        dateMillis = timestamp,
                        timeState = timePickerState
                    )
                    onTimestampChange(updatedTimestamp)
                    isDialogTimeVisible = false
                },
                onDismiss = { isDialogTimeVisible = false },
                initialHour = dateTime?.hour ?: LocalTime.now().plusHours(1).hour,
                initialMinute = dateTime?.minute ?: 0,
            )
        }

        if (isDialogDateVisible) {
            DatePicker(
                onConfirm = { selectedDateMillis ->
                    val updatedTimestamp = combineDateAndTime(
                        dateMillis = selectedDateMillis,
                        timeState = timePickerState
                    )
                    onTimestampChange(updatedTimestamp)
                    isDialogDateVisible = false
                },
                onDismiss = { isDialogDateVisible = false },
                initialSelectedDateMillis = timestamp ?: Instant.now().toEpochMilli(),
            )
        }
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