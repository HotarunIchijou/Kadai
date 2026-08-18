package org.kaorun.kadai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.kaorun.kadai.data.repository.UserPreferencesRepository
import org.kaorun.kadai.ui.navigation.MainRoute
import org.kaorun.kadai.ui.navigation.NavRoute
import org.kaorun.kadai.ui.navigation.PermissionRoute
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = userPreferencesRepository.isOnboardingCompleted
        .map { isCompleted ->
            MainActivityUiState.Success(
                startRoute = if (isCompleted) MainRoute else PermissionRoute
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainActivityUiState.Loading
        )

    fun completeOnboarding() {
        viewModelScope.launch {
            userPreferencesRepository.setOnboardingCompleted(true)
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val startRoute: NavRoute) : MainActivityUiState
}