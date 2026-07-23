package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.AppBarWithSearchColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.filter_list
import org.kaorun.kadai.ui.icons.menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navigationRailState: WideNavigationRailState,
    scope: CoroutineScope
) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        AppBarWithSearch(
            state = searchBarState,
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch { navigationRailState.expand() }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = menu,
                        contentDescription = stringResource(R.string.menu)
                    )
                }
            },
            inputField = @Composable {
                SearchBarDefaults.InputField(
                    textFieldState = textFieldState,
                    searchBarState = searchBarState,
                    onSearch = {
                        scope.launch { searchBarState.animateToCollapsed() }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .clearAndSetSemantics { }
                                .fillMaxWidth(),
                        )
                    }
                )
            },
            actions = {
                IconButton({}) {
                    Icon(
                        imageVector = filter_list,
                        contentDescription = stringResource(R.string.menu)
                    )
                }
            },
            colors = AppBarWithSearchColors(
                searchBarColors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                ),
                scrolledSearchBarContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                appBarContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                scrolledAppBarContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                appBarNavigationIconColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                appBarActionIconColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor
            ),
            modifier = Modifier.statusBarsPadding()
        )
    }
}