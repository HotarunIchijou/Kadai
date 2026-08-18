package org.kaorun.kadai.ui.screens.task.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.done_all
import org.kaorun.kadai.ui.icons.remove_done

@Composable
fun ExtendedFloatingActionButton(
    onClick: () -> Unit,
    isVisible: Boolean,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val text = if (!isCompleted) {
        stringResource(R.string.mark_as_completed)
    } else stringResource(R.string.unmark_as_completed)
    val icon = if (!isCompleted) done_all else remove_done

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(animationSpec = tween(150)),
        exit = scaleOut(animationSpec = tween(150)),
    ) {
        ExtendedFloatingActionButton(
            text = {
                Text(text)
            },
            icon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            },
            onClick = onClick,
            modifier = modifier,
            containerColor = if (!isCompleted) {
                MaterialTheme.colorScheme.primaryContainer
            } else MaterialTheme.colorScheme.tertiaryContainer
        )
    }
}