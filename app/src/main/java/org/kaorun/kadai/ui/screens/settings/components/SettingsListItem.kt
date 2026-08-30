package org.kaorun.kadai.ui.screens.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.ui.icons.check
import org.kaorun.kadai.ui.icons.close

@Composable
fun ListItem(
    item: SettingsItemUiState,
    index: Int,
    count: Int,
    modifier: Modifier = Modifier
) {
    val cornerSize = 20.dp
    val innerCornerSize = 4.dp
    val colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceBright)

    val shape = remember(index, count) {
        when {
            count == 1 -> RoundedCornerShape(cornerSize)
            index == 0 -> RoundedCornerShape(
                topStart = cornerSize,
                topEnd = cornerSize,
                bottomStart = innerCornerSize,
                bottomEnd = innerCornerSize
            )

            index == count - 1 -> RoundedCornerShape(
                topStart = innerCornerSize,
                topEnd = innerCornerSize,
                bottomStart = cornerSize,
                bottomEnd = cornerSize
            )

            else -> RoundedCornerShape(innerCornerSize)
        }
    }

    val onClick: () -> Unit = when (item) {
        is SettingsItemUiState.Click -> item.onClick
        is SettingsItemUiState.Switch -> {
            { item.onCheckedChange(!item.isChecked) }
        }
    }

    SegmentedListItem(
        onClick = onClick,
        shapes = ListItemDefaults.segmentedShapes(
            index = index,
            count = count
        ),
        modifier = modifier.clip(shape),
        colors = colors,
        leadingContent = item.icon?.let { icon ->
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        },
        content = {
            Text(
                text = item.title,
                modifier = Modifier.padding(end = 16.dp),
                maxLines = 3,
                style = typography.titleMediumEmphasized
            )
        },
        supportingContent = item.subtitle?.let { subtitle ->
            {
                Text(
                    text = subtitle,
                    modifier = Modifier.padding(end = 16.dp),
                    maxLines = 5,
                    style = typography.bodyMedium
                )
            }
        },
        trailingContent = when (item) {
            is SettingsItemUiState.Switch -> {
                {
                    Switch(
                        checked = item.isChecked,
                        onCheckedChange = item.onCheckedChange,
                        thumbContent = {
                            Icon(
                                imageVector = if (item.isChecked) check
                                else close,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        }
                    )
                }
            }

            is SettingsItemUiState.Click -> null
        },
        verticalAlignment = Alignment.CenterVertically
    )
}