package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.material3.Icon
import androidx.compose.material3.ModalWideNavigationRail
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.filled.settingsFilled
import org.kaorun.kadai.ui.icons.filled.task_alt_filled
import org.kaorun.kadai.ui.icons.settings
import org.kaorun.kadai.ui.icons.task_alt

@Composable
fun ModalWideNavigationRail(
    navigationRailState: WideNavigationRailState,
    scope: CoroutineScope
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf(stringResource(R.string.tasks), stringResource(R.string.settings))
    val selectedIcons = listOf(task_alt_filled, settingsFilled)
    val unselectedIcons = listOf(task_alt, settings)
    ModalWideNavigationRail(
        state = navigationRailState,
        hideOnCollapse = true
    ) {
        items.forEachIndexed { index, item ->
            WideNavigationRailItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    scope.launch { navigationRailState.collapse() }
                },
                icon = {
                    Icon(
                        imageVector = if (selectedItem == index) selectedIcons[index]
                        else unselectedIcons[index],
                        contentDescription = null
                    )
                },
                label = {
                    Text(item)
                },
                railExpanded = true

            )
        }
    }
}