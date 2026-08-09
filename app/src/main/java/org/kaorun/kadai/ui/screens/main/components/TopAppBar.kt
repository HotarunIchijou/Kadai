@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.AppBarWithSearchColors
import androidx.compose.material3.ExpandedFullScreenContainedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.appBarWithSearchColors
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.rememberContainedSearchBarState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.arrow_back
import org.kaorun.kadai.ui.icons.close
import org.kaorun.kadai.ui.icons.filter_list
import org.kaorun.kadai.ui.icons.menu

@Composable
fun TopAppBar(
    navigationRailState: WideNavigationRailState,
    onSearch: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val searchBarState = rememberContainedSearchBarState()
    val textFieldState = rememberTextFieldState()
    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            onSearch = {
                scope.launch {
                    onSearch(textFieldState.text.toString())
                    searchBarState.animateToCollapsed()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                AnimatedVisibility(
                    visible = searchBarState.currentValue == SearchBarValue.Collapsed,
                    enter = fadeIn(animationSpec = motionScheme.defaultEffectsSpec()),
                    exit = fadeOut(animationSpec = motionScheme.defaultEffectsSpec())
                ) {
                    Text(
                        modifier = Modifier.clearAndSetSemantics { }.fillMaxWidth(),
                        text = stringResource(R.string.search),
                        textAlign = TextAlign.Center
                    )
                }
            },
            leadingIcon = {
                SearchBarIcon(
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd("")
                        onSearch(textFieldState.text.toString())
                        scope.launch { searchBarState.animateToCollapsed() }
                    },
                    imageVector = arrow_back,
                    contentDescription = stringResource(R.string.back),
                    isVisible = searchBarState.targetValue == SearchBarValue.Expanded
                            || textFieldState.text.isNotBlank(),
                )
            },
            trailingIcon = {
                SearchBarIcon(
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd("")
                    },
                    imageVector = close,
                    contentDescription = stringResource(R.string.clear),
                    isVisible = searchBarState.targetValue == SearchBarValue.Expanded
                            && textFieldState.text.isNotBlank()
                )
            },
            colors = appBarWithSearchColors().searchBarColors.inputFieldColors
        )
    }

    Surface(
        color = colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        AppBarWithSearch(
            state = searchBarState,
            inputField = inputField,
            navigationIcon = {
                TopAppBarIcon(
                    onClick = {
                        scope.launch {
                            if (navigationRailState.targetValue == WideNavigationRailValue.Expanded)
                                navigationRailState.collapse()
                            else navigationRailState.expand()
                        }
                    },
                    imageVector = menu,
                    contentDescription = stringResource(R.string.menu),
                    isStart = true,
                    isVisible = searchBarState.targetValue == SearchBarValue.Collapsed
                )
            },
            actions = {
                TopAppBarIcon(
                    onClick = {  },
                    imageVector = filter_list,
                    contentDescription = stringResource(R.string.filter),
                    isStart = false,
                    isVisible = searchBarState.targetValue == SearchBarValue.Collapsed
                )
            },
            colors = AppBarWithSearchColors(
                searchBarColors = SearchBarDefaults.colors(
                    containerColor = colorScheme.surfaceContainerHighest
                ),
                scrolledSearchBarContainerColor = colorScheme.surfaceContainerHighest,
                appBarContainerColor = colorScheme.surfaceContainer,
                scrolledAppBarContainerColor = colorScheme.surfaceContainer,
                appBarNavigationIconColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                appBarActionIconColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor
            ),
            modifier = Modifier.statusBarsPadding()
        )

        ExpandedFullScreenContainedSearchBar(
            state = searchBarState,
            inputField = inputField,
            colors = appBarWithSearchColors().searchBarColors
        ) {
            HorizontalDivider()
        }
    }
}

@Composable
private fun SearchBarIcon(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    isVisible: Boolean
) = AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = motionScheme.fastEffectsSpec()),
        exit = fadeOut(animationSpec = motionScheme.fastEffectsSpec())
    ) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                positioning = TooltipAnchorPosition.Below
            ),
            tooltip = {
                PlainTooltip(
                    modifier = Modifier.semantics {
                        liveRegion = LiveRegionMode.Assertive
                        paneTitle = contentDescription
                    }
                ) {
                    Text(contentDescription)
                }
            },
            state = rememberTooltipState()
        ) {
            IconButton(onClick = onClick) {
                Icon(imageVector = imageVector, contentDescription = contentDescription)
            }
        }
    }

@Composable
private fun TopAppBarIcon(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    isStart: Boolean,
    isVisible: Boolean
) {
    val offset = { x: IntSize ->
        IntOffset(
            x = if (isStart) -x.width else x.width,
            y = 0
        )
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = slideIn(
            animationSpec = motionScheme.fastSpatialSpec(),
            initialOffset = offset
        ),
        exit = slideOut(
            animationSpec = tween(durationMillis = 150, delayMillis = 0),
            targetOffset = offset
        )
    ) {
        IconButton(
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = colorScheme.onSurfaceVariant
            )
        ) {
            Icon(imageVector = imageVector, contentDescription = contentDescription)
        }
    }
}