package org.kaorun.kadai.ui.screens.settings.appearance.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.R.string
import org.kaorun.kadai.data.model.ThemeMode
import org.kaorun.kadai.ui.icons.brightness_6
import org.kaorun.kadai.ui.icons.filled.check_circle_filled

@Composable
fun ThemeSelectionDialog(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val themes = ThemeMode.entries

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                shapes = ButtonDefaults.shapes()
            ) {
                Text(stringResource(string.cancel))
            }
        },
        icon = {
            Icon(
                imageVector = brightness_6,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text(
                text = stringResource(string.theme_title),
                style = MaterialTheme.typography.headlineSmallEmphasized
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
            ) {
                themes.forEachIndexed { _, theme ->
                    val selected = theme == currentTheme
                    ListItem(
                        selected = selected,
                        onClick = {
                            onThemeSelected(theme)
                            onDismiss()
                        },
                        content = {
                            Text(
                                text = stringResource(theme.titleRes),
                            )
                        },
                        trailingContent = {
                            AnimatedVisibility(
                                visible = selected,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Icon(
                                    imageVector = check_circle_filled,
                                    contentDescription = null,
                                    tint = colorScheme.onSecondaryContainer
                                )
                            }
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        colors = ListItemDefaults.colors(
                            containerColor = colorScheme.surfaceContainerHigh
                        )
                    )
                }
            }
        }
    )
}