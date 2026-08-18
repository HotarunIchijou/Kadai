@file:OptIn(ExperimentalMaterial3Api::class)

package org.kaorun.kadai.ui.screens.permission.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.close

@Composable
fun PermissionCloseButton(
    onClose: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(targetValue = if (isVisible) 1f else 0f, label = "closeButtonAlpha")

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Below
        ),
        tooltip = {
            PlainTooltip {
                Text(stringResource(R.string.close))
            }
        },
        state = rememberTooltipState(),
        modifier = modifier
    ) {
        IconButton(
            onClick = onClose,
            enabled = isVisible,
            shapes = IconButtonDefaults.shapes(),
            modifier = Modifier
                .padding(start = 20.dp, top = 8.dp)
                .alpha(alpha),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Icon(
                imageVector = close,
                contentDescription = stringResource(R.string.close)
            )
        }
    }
}