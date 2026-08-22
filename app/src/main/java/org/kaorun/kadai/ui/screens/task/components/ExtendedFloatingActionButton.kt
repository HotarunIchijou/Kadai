package org.kaorun.kadai.ui.screens.task.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        modifier = modifier.animateFloatingActionButton(
            visible = isVisible,
            alignment = Alignment.BottomEnd
        ),
        containerColor = if (!isCompleted) {
            MaterialTheme.colorScheme.primaryContainer
        } else MaterialTheme.colorScheme.tertiaryContainer
    )
}