package org.kaorun.kadai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import org.kaorun.kadai.ui.navigation.DeepLinkParser
import org.kaorun.kadai.ui.navigation.NavRoute
import org.kaorun.kadai.ui.navigation.Navigation
import org.kaorun.kadai.ui.theme.KadaiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var deepLinkRoute by mutableStateOf<NavRoute?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        deepLinkRoute = DeepLinkParser.parseKey(intent)

        setContent {
            KadaiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Navigation(initialDeepLink = deepLinkRoute)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}