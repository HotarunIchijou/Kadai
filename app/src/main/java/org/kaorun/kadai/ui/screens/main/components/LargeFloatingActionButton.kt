package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.add_task

@Composable
fun LargeFloatingActionButton(onClick: () -> Unit) {
    LargeFloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = add_task,
            contentDescription = stringResource(R.string.add_task),
            modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize)
        )
    }
}