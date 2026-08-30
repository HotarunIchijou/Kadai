package org.kaorun.kadai.ui.screens.settings.appearance

import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kaorun.kadai.R.string
import org.kaorun.kadai.ui.icons.brightness_6
import org.kaorun.kadai.ui.icons.colors
import org.kaorun.kadai.ui.screens.settings.appearance.components.ThemeSelectionDialog
import org.kaorun.kadai.ui.screens.settings.components.SettingsItemUiState
import org.kaorun.kadai.ui.screens.settings.components.SettingsItemUiState.Click
import org.kaorun.kadai.ui.screens.settings.components.SettingsItemUiState.Switch
import org.kaorun.kadai.ui.screens.settings.components.SettingsList
import org.kaorun.kadai.ui.screens.settings.components.TopAppBar
import org.kaorun.kadai.ui.theme.KadaiTheme

@Composable
fun SettingsAppearanceScreen(
    viewModel: SettingsAppearanceViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var showThemeDialog by rememberSaveable { mutableStateOf(false) }
    val isDynamicTheme by viewModel.isDynamicTheme.collectAsStateWithLifecycle()

    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val items = buildList {
        add(
            Click(
                title = stringResource(string.theme_title),
                subtitle = stringResource(themeMode.titleRes),
                icon = brightness_6,
                onClick = { showThemeDialog = true }
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(
                Switch(
                    title = stringResource(string.dynamic_theme_title),
                    subtitle = stringResource(string.dynamic_theme_summary),
                    icon = colors,
                    isChecked = isDynamicTheme,
                    onCheckedChange = viewModel::onDynamicThemeToggled
                )
            )
        }
    }


    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = themeMode,
            onThemeSelected = viewModel::onThemeSelected,
            onDismiss = { showThemeDialog = false }
        )
    }

    SettingsAppearanceScreenContent(
        items = items,
        onBack = onBack
    )
}

@Composable
fun SettingsAppearanceScreenContent(
    items: List<SettingsItemUiState>,
    onBack: () -> Unit,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = topAppBarState
    )

    LaunchedEffect(topAppBarState.heightOffsetLimit) {
        if (topAppBarState.heightOffsetLimit != 0f) {
            topAppBarState.heightOffset = topAppBarState.heightOffsetLimit
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(string.appearance_title),
                scrollBehavior = scrollBehavior,
                onBack = onBack
            )
        },
        containerColor = colorScheme.surfaceContainer,
    ) { contentPadding ->
        SettingsList(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            items = items
        )
    }
}

@Composable
fun SettingsAppearanceScreenPreview() {
    KadaiTheme {
        SettingsAppearanceScreenContent(
            items = emptyList(),
            onBack = { }
        )
    }
}