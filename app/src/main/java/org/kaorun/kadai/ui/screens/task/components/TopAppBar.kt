package org.kaorun.kadai.ui.screens.task.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ButtonGroupScope
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
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
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.rememberTooltipState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kaorun.kadai.ui.icons.close
import org.kaorun.kadai.ui.icons.schedule
import org.kaorun.kadai.ui.icons.calendar_month
import org.kaorun.kadai.ui.icons.arrow_back
import org.kaorun.kadai.ui.icons.delete
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.done_all
import org.kaorun.kadai.ui.icons.more_vert
import org.kaorun.kadai.ui.icons.remove_done
import org.kaorun.kadai.ui.theme.KadaiTheme
import org.kaorun.kadai.ui.utils.combineDateAndTime
import org.kaorun.kadai.ui.utils.toFormattedDate
import org.kaorun.kadai.ui.utils.toFormattedTime
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId


@Composable
fun TopAppBar(
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    TooltipAnchorPosition.Below
                ),
                tooltip = {
                    PlainTooltip {
                        Text(stringResource(R.string.back))
                    }
                },
                state = rememberTooltipState(),
            ) {
                IconButton(
                    onClick = { onBack() },
                    shapes = IconButtonDefaults.shapes(),
                    modifier = Modifier.padding(start = 8.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                ) {
                    Icon(
                        imageVector = arrow_back,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            MenuButton(
                onDeleteClick = onDelete,
                onBack = onBack
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        contentPadding = TopAppBarDefaults.ContentPadding,
    )
}

@Composable
private fun MenuButton(
    onDeleteClick: () -> Unit,
    onBack: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Below
        ),
        tooltip = {
            PlainTooltip {
                Text(stringResource(R.string.open_menu))
            }
        },
        state = rememberTooltipState()
    ) {
        IconButton(
            onClick = { expanded = true },
            shapes = IconButtonDefaults.shapes(),
            modifier = Modifier.minimumInteractiveComponentSize().size(
                IconButtonDefaults.smallContainerSize(
                    IconButtonDefaults.IconButtonWidthOption.Narrow
                )
            ),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Icon(
                imageVector = more_vert,
                contentDescription = stringResource(R.string.open_menu)
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            if (expanded) expanded = false
        },
        shape = RoundedCornerShape(12.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
    ) {
        DropdownMenuItem(
            text = {
                Text(stringResource(R.string.delete))
            },
            onClick = {
                onDeleteClick()
                if (expanded) expanded = false
                onBack()
            },
            leadingIcon = {
                Icon(
                    imageVector = delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        )
    }
}