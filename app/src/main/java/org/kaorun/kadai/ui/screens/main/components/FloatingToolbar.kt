package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingToolbar(
    onItemSelected: (Int) -> Unit,
    items: Map<ImageVector, String>,
    selectedIndex: Int
) {
    HorizontalFloatingToolbar(
        expanded = true,
        modifier = Modifier.height(56.dp),
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
            toolbarContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        contentPadding = PaddingValues(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.entries.forEachIndexed { index, (icon, title) ->
                val selected = index == selectedIndex

                FilterChip(
                    selected = selected,
                    onClick = { onItemSelected(index) },
                    label = {
                        Text(
                            text = title,
                            style = if (selected) {
                                MaterialTheme.typography.bodyMediumEmphasized
                            } else {
                                MaterialTheme.typography.bodyMedium
                            }
                        )
                    },
                    modifier = Modifier.heightIn(min = 40.dp),
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(),
                    shape = CircleShape,
                    border = null,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                )
            }
        }
    }
}