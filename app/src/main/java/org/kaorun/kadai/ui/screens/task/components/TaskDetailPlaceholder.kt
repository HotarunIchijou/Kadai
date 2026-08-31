package org.kaorun.kadai.ui.screens.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.R.string

@Composable
fun TaskDetailPlaceholder(modifier: Modifier = Modifier) {
    val insets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Top + WindowInsetsSides.End + WindowInsetsSides.Bottom
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(insets)
            .consumeWindowInsets(WindowInsets.safeDrawing)
            .padding(end = 16.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(colorScheme.surfaceContainerLow),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(string.task_edit_placeholder),
            style = typography.bodyLarge,
            color = colorScheme.onSurfaceVariant
        )
    }
}