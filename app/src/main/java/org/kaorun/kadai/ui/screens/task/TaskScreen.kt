@file:OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class
)

package org.kaorun.kadai.ui.screens.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigation3.LocalListDetailSceneScope
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.calendar_month
import org.kaorun.kadai.ui.icons.schedule
import org.kaorun.kadai.ui.screens.permission.utils.rememberPermissionsGranted
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
import org.kaorun.kadai.ui.utils.toFormattedFullDateTime
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
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val isWideScreen =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val isDetailPane = (LocalListDetailSceneScope.current != null) || isWideScreen

    val containerColor = if (isDetailPane) {
        colorScheme.surfaceContainerLow
    } else {
        colorScheme.surfaceContainer
    }

    val paneShape = if (isDetailPane) RoundedCornerShape(28.dp) else RectangleShape
    val insets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Top + WindowInsetsSides.End + WindowInsetsSides.Bottom
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (isDetailPane) {
                    Modifier
                        .windowInsetsPadding(insets)
                        .consumeWindowInsets(WindowInsets.safeDrawing)
                        .padding(end = 16.dp)
                        .clip(paneShape)
                } else {
                    Modifier
                }
            ),
        shape = paneShape,
        color = containerColor
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        when (val state = uiState) {
            is TaskUiState.Loading -> Unit
            is TaskUiState.Success -> {
                TaskScreenContent(
                    modifier = modifier,
                    title = state.title,
                    details = state.details,
                    createdAtTimestamp = state.createdAtTimestamp,
                    modifiedAtTimestamp = state.modifiedAtTimestamp,
                    dueTimestamp = state.dueTimestamp,
                    isDone = state.isDone,
                    isNewTask = state.isNewTask,
                    isDetailPane = isDetailPane,
                    containerColor = containerColor,
                    onClose = { viewModel.onBack(navigateBack = onBack) },
                    onTitleChange = viewModel::onTitleChange,
                    onDetailsChange = viewModel::onDetailsChange,
                    onTimestampChange = viewModel::onTimestampChange,
                    onDoneChange = viewModel::onDoneChange,
                    onDelete = viewModel::onDelete
                )
            }
        }
    }
}

@Composable
fun TaskScreenContent(
    modifier: Modifier = Modifier,
    title: String,
    details: String?,
    createdAtTimestamp: Long,
    modifiedAtTimestamp: Long?,
    dueTimestamp: Long?,
    isDone: Boolean,
    isNewTask: Boolean = false,
    isDetailPane: Boolean = false,
    containerColor: Color = colorScheme.surfaceContainer,
    onTitleChange: (String) -> Unit,
    onDetailsChange: (String) -> Unit,
    onTimestampChange: (Long?) -> Unit,
    onDoneChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val dateTime = remember(dueTimestamp) {
        dueTimestamp?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()) }
    }
    val permissionsGranted = rememberPermissionsGranted()
    val timePickerState = rememberTimePickerState(
        initialHour = dateTime?.hour ?: LocalTime.now().plusHours(1).hour,
        initialMinute = dateTime?.minute ?: 0
    )
    val isTimeChecked = dueTimestamp != null
    val isDateChecked = dateTime?.let {
        it.toLocalDate() != LocalDate.now(ZoneId.systemDefault())
    } ?: false

    val createdAt = remember(createdAtTimestamp) {
        createdAtTimestamp.toFormattedFullDateTime(context)
    }
    val createdAtText = stringResource(R.string.created_at, createdAt)

    val lastModified = remember(modifiedAtTimestamp, createdAtTimestamp) {
        (modifiedAtTimestamp ?: createdAtTimestamp).toFormattedFullDateTime(context)
    }
    val lastModifiedText = if (!isNewTask) {
        stringResource(R.string.last_modified, lastModified)
    } else null

    var isDialogTimeVisible by rememberSaveable { mutableStateOf(false) }
    var isDialogDateVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = if (isDetailPane) {
            WindowInsets(0, 0, 0, 0)
        } else {
            ScaffoldDefaults.contentWindowInsets
        },
        topBar = {
            TopAppBar(
                onBack = onClose,
                onDelete = onDelete,
                containerColor = containerColor,
                windowInsets = if (isDetailPane) {
                    WindowInsets(0, 0, 0, 0)
                } else {
                    TopAppBarDefaults.windowInsets
                }
            )
        },
        containerColor = containerColor,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onDoneChange(!isDone) },
                isCompleted = isDone,
                isVisible = title.isNotBlank() || details?.isNotBlank() == true,
                modifier = Modifier.imePadding()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp, horizontal = 28.dp)
                .padding(bottom = 80.dp)
        ) {
            TextField(
                text = title,
                sampleText = stringResource(R.string.title),
                textStyle = typography.headlineMediumEmphasized,
                onValueChange = onTitleChange,
                modifier = modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = createdAtText,
                    style = typography.labelMedium,
                    color = colorScheme.onSurfaceVariant
                )

                lastModifiedText?.let {
                    Text(
                        text = it,
                        style = typography.labelMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }

            val dateString = dueTimestamp?.toFormattedDate(context)
                ?: stringResource(R.string.task_date)
            val timeString = dueTimestamp?.toFormattedTime(context)
                ?: stringResource(R.string.task_time)

            ButtonGroup(
                overflowIndicator = { menuState ->
                    ButtonGroupDefaults.OverflowIndicator(menuState = menuState)
                },
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                buttonGroupToggleItem(
                    onClick = { isDialogTimeVisible = true },
                    enabled = permissionsGranted,
                    imageVector = schedule,
                    text = timeString,
                    isChecked = isTimeChecked
                )

                if (isTimeChecked) {
                    buttonGroupIconItem(
                        onClick = { onTimestampChange(null) }
                    )
                }

                dueTimestamp?.let {
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

            HorizontalDivider(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            TextField(
                text = details ?: "",
                sampleText = stringResource(R.string.details),
                textStyle = typography.bodyLarge,
                onValueChange = onDetailsChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isDialogTimeVisible) {
            TimePicker(
                onConfirm = { pickerState ->
                    val updatedTimestamp = combineDateAndTime(
                        dateMillis = dueTimestamp,
                        timeState = pickerState
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
                initialSelectedDateMillis = dueTimestamp ?: Instant.now().toEpochMilli(),
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
            createdAtTimestamp = Instant.now().toEpochMilli(),
            modifiedAtTimestamp = null,
            dueTimestamp = null,
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