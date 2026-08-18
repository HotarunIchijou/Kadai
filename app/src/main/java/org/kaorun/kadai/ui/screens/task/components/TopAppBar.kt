@file:OptIn(ExperimentalMaterial3Api::class)

package org.kaorun.kadai.ui.screens.task.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.arrow_back
import org.kaorun.kadai.ui.icons.delete
import org.kaorun.kadai.ui.icons.more_vert


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