@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package org.kaorun.kadai.ui.screens.task.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.close

fun ButtonGroupScope.buttonGroupToggleItem(
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

fun ButtonGroupScope.buttonGroupIconItem(
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