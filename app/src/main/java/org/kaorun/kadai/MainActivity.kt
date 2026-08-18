package org.kaorun.kadai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.kaorun.kadai.ui.navigation.DeepLinkParser
import org.kaorun.kadai.ui.navigation.NavRoute
import org.kaorun.kadai.ui.navigation.Navigation
import org.kaorun.kadai.ui.theme.KadaiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private var deepLinkRoute by mutableStateOf<NavRoute?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value is MainActivityUiState.Loading
        }

        deepLinkRoute = DeepLinkParser.parseKey(intent)

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            KadaiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    when (val state = uiState) {
                        is MainActivityUiState.Loading -> { }
                        is MainActivityUiState.Success -> {
                            Navigation(
                                startRoute = state.startRoute,
                                onCompleteOnboarding = viewModel::completeOnboarding,
                                initialDeepLink = deepLinkRoute
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        deepLinkRoute = DeepLinkParser.parseKey(intent)
    }
}