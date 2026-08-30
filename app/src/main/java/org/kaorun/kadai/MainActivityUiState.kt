package org.kaorun.kadai

import org.kaorun.kadai.ui.navigation.NavRoute

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val startRoute: NavRoute) : MainActivityUiState
}