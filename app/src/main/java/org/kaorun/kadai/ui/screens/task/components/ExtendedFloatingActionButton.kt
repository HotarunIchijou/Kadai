package org.kaorun.kadai.ui.screens.task.components

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
    isDone: Boolean,
    modifier: Modifier = Modifier
) {
    val text = if (!isDone) {
        stringResource(R.string.mark_as_completed)
    } else stringResource(R.string.unmark_as_completed)
    val contentDescription = if (!isDone) {
        stringResource(R.string.mark_done)
    } else stringResource(R.string.mark_undone)
    val icon = if (!isDone) done_all else remove_done

    ExtendedFloatingActionButton(
        text = {
            Text(text)
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        },
        onClick = onClick,
        modifier = modifier,
        containerColor = if (!isDone) {
            MaterialTheme.colorScheme.primaryContainer
        } else MaterialTheme.colorScheme.tertiaryContainer
    )
}