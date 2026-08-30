package org.kaorun.kadai.ui.screens.settings.about

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.bug_report
import org.kaorun.kadai.ui.icons.code_xml
import org.kaorun.kadai.ui.icons.info
import org.kaorun.kadai.ui.icons.license
import org.kaorun.kadai.ui.screens.settings.about.utils.AboutLinks
import org.kaorun.kadai.ui.screens.settings.about.utils.getAppVersion
import org.kaorun.kadai.ui.screens.settings.components.SettingsItemUiState
import org.kaorun.kadai.ui.screens.settings.components.SettingsItemUiState.Click
import org.kaorun.kadai.ui.screens.settings.components.SettingsList
import org.kaorun.kadai.ui.screens.settings.components.TopAppBar
import org.kaorun.kadai.ui.theme.KadaiTheme

@Composable
fun SettingsAboutScreen(
    onBack: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val items = listOf(
        Click(
            title = stringResource(R.string.version_title),
            subtitle = getAppVersion(),
            icon = info,
            onClick = { uriHandler.openUri(AboutLinks.GITHUB_VERSION) }
        ),
        Click(
            title = stringResource(R.string.source_code_title),
            subtitle = stringResource(R.string.source_code_summary),
            icon = code_xml,
            onClick = { uriHandler.openUri(AboutLinks.GITHUB_REPO) }
        ),
        Click(
            title = stringResource(R.string.issues_title),
            subtitle = stringResource(R.string.issues_summary),
            icon = bug_report,
            onClick = { uriHandler.openUri(AboutLinks.GITHUB_ISSUES) }
        ),
        Click(
            title = stringResource(R.string.license_title),
            subtitle = stringResource(R.string.license_summary),
            icon = license,
            onClick = { uriHandler.openUri(AboutLinks.GITHUB_LICENSE) }

        ),
        Click(
            title = stringResource(R.string.contact_developer_title),
            subtitle = stringResource(R.string.contact_developer_summary),
            icon = ImageVector.vectorResource(R.drawable.telegram_24px),
            onClick = { uriHandler.openUri(AboutLinks.TELEGRAM) }
        )
    )
    SettingsAboutScreenContent(
        items = items,
        onBack = onBack
    )
}

@Composable
fun SettingsAboutScreenContent(
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
                title = stringResource(R.string.about_title),
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
fun SettingsAboutScreenPreview() {
    KadaiTheme {
        SettingsAboutScreenContent(
            items = emptyList(),
            onBack = { }
        )
    }
}