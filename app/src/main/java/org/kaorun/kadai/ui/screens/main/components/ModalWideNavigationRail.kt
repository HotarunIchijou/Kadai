package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.material3.ModalWideNavigationRail
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.filled.settings_filled
import org.kaorun.kadai.ui.icons.filled.task_alt_filled
import org.kaorun.kadai.ui.icons.settings
import org.kaorun.kadai.ui.icons.task_alt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalWideNavigationRail(
    navigationRailState: WideNavigationRailState,
    onNavigateToSettings: () -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(stringResource(R.string.tasks), stringResource(R.string.settings))
    val selectedIcons = listOf(task_alt_filled, settings_filled)
    val unselectedIcons = listOf(task_alt, settings)
    val scope = rememberCoroutineScope()

    ModalWideNavigationRail(state = navigationRailState) {
        items.forEachIndexed { index, item ->
            AnimatedVisibility(
                visible = navigationRailState.targetValue == WideNavigationRailValue.Expanded,
                enter = fadeIn(animationSpec = motionScheme.slowEffectsSpec()),
                exit = fadeOut(animationSpec = motionScheme.slowEffectsSpec())
            ) {
                WideNavigationRailItem(
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        scope.launch { navigationRailState.collapse() }
                        if (selectedItem == 1) {
                            onNavigateToSettings()
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selectedItem == index) selectedIcons[index]
                            else unselectedIcons[index],
                            contentDescription = item
                        )
                    },
                    label = { Text(item) },
                    railExpanded = true
                )
            }
        }
    }
}