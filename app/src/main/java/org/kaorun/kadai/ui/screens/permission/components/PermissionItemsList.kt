package org.kaorun.kadai.ui.screens.permission.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.cancel
import org.kaorun.kadai.ui.icons.check_circle

data class PermissionItem(
    val title: String,
    val isGranted: Boolean,
    val onClick: () -> Unit
)

private fun segmentShape(index: Int, count: Int, outer: Dp = 20.dp, inner: Dp = 4.dp) = when (index) {
    0 -> RoundedCornerShape(topStart = outer, topEnd = outer, bottomStart = inner, bottomEnd = inner)
    count - 1 -> RoundedCornerShape(topStart = inner, topEnd = inner, bottomStart = outer, bottomEnd = outer)
    else -> RoundedCornerShape(inner)
}

@Composable
fun PermissionItemsList(
    items: List<PermissionItem>,
    modifier: Modifier = Modifier
) {
    val colors = ListItemDefaults.colors(containerColor = colorScheme.surfaceBright)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        items.forEachIndexed { index, item ->
            val summary = stringResource(
                if (item.isGranted) R.string.granted
                else R.string.not_granted
            )
            val trailingIcon = @Composable {
                if (item.isGranted) {
                    Icon(
                        imageVector = check_circle,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = colorScheme.primary
                    )
                } else {
                    Icon(
                        imageVector = cancel,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = colorScheme.error
                    )
                }
            }

            SegmentedListItem(
                onClick = { if (!item.isGranted) item.onClick() },
                shapes = ListItemDefaults.segmentedShapes(index = index, count = items.size),
                modifier = Modifier.clip(segmentShape(index, items.size)),
                colors = colors,
                content = { Text(text = item.title) },
                supportingContent = {
                    Text(
                        text = summary,
                        color = if (item.isGranted) colorScheme.primary else colorScheme.onSurfaceVariant
                    )
                },
                trailingContent = { trailingIcon() }
            )
        }
    }
}