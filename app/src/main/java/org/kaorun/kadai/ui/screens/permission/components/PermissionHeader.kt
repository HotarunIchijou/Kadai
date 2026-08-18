package org.kaorun.kadai.ui.screens.permission.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.toPath

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PermissionHeader(
    icon: ImageVector,
    title: String,
    summary: String,
    modifier: Modifier = Modifier
) {
    val primaryContainer = colorScheme.primaryContainer

    Box(
        modifier = modifier
            .drawWithCache {
                val path = MaterialShapes.Cookie7Sided.toPath().asComposePath()
                path.transform(Matrix().apply { scale(size.width, size.height) })
                onDrawBehind { drawPath(path = path, color = primaryContainer) }
            }
            .padding(72.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(128.dp),
            tint = colorScheme.onPrimaryContainer
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = title,
        style = typography.headlineMedium,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = summary,
        style = typography.bodyLarge,
        color = colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )
}