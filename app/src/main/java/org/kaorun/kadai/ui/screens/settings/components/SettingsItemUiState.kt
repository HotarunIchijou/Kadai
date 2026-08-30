package org.kaorun.kadai.ui.screens.settings.components

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface SettingsItemUiState {
    val title: String
    val subtitle: String?
    val icon: ImageVector?

    data class Switch(
        override val title: String,
        override val subtitle: String? = null,
        override val icon: ImageVector? = null,
        val isChecked: Boolean,
        val onCheckedChange: (Boolean) -> Unit
    ) : SettingsItemUiState

    data class Click(
        override val title: String,
        override val subtitle: String? = null,
        override val icon: ImageVector? = null,
        val onClick: () -> Unit
    ) : SettingsItemUiState
}