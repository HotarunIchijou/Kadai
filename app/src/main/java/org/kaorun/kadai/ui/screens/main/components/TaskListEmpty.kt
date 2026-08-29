package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.toPath

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskListEmpty(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    val surfaceContainerLowest = colorScheme.surfaceContainerLowest

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .drawWithCache {
                    val path = MaterialShapes.Cookie9Sided.toPath().asComposePath()
                    path.transform(Matrix().apply { scale(size.width, size.height) })
                    onDrawBehind { drawPath(path = path, color = surfaceContainerLowest) }
                }
                .size(240.dp)
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(128.dp))
                    .background(colorScheme.secondaryContainer)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    tint = colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = modifier.height(16.dp))

        Text(
            text = title,
            style = typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}