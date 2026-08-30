package org.kaorun.kadai.ui.screens.settings.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.info
import org.kaorun.kadai.ui.icons.notification_settings
import org.kaorun.kadai.ui.icons.palette
import org.kaorun.kadai.ui.screens.settings.components.SettingsItemUiState
import org.kaorun.kadai.ui.screens.settings.components.SettingsItemUiState.Click
import org.kaorun.kadai.ui.screens.settings.components.SettingsList
import org.kaorun.kadai.ui.screens.settings.components.TopAppBar
import org.kaorun.kadai.ui.theme.KadaiTheme

@Composable
fun SettingsScreen(
    onNavigateToAppearance: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onBack: () -> Unit
) {
    val items = listOf(
        Click(
            title = stringResource(R.string.appearance_title),
            subtitle = stringResource(R.string.appearance_summary),
            icon = palette,
            onClick = onNavigateToAppearance
        ),
        Click(
            title = stringResource(R.string.notifications_title),
            subtitle = stringResource(R.string.notifications_summary),
            icon = notification_settings,
            onClick = onNavigateToNotifications
        ),
        Click(
            title = stringResource(R.string.about_title),
            subtitle = stringResource(R.string.about_summary),
            icon = info,
            onClick = onNavigateToAbout
        )
    )
    SettingsScreenContent(
        items = items,
        onBack = onBack
    )
}

@Composable
fun SettingsScreenContent(
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
                title = stringResource(R.string.settings),
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
fun SettingsScreenPreview() {
    KadaiTheme {
        SettingsScreenContent(
            items = emptyList(),
            onBack = { }
        )
    }
}