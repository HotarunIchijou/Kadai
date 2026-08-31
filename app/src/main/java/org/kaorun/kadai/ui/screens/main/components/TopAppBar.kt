@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3AdaptiveApi::class
)

package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.AppBarWithSearchColors
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExpandedFullScreenContainedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.appBarWithSearchColors
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.SelectableDropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.rememberContainedSearchBarState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.window.core.layout.WindowSizeClass
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.data.model.TaskSortConfig
import org.kaorun.kadai.data.model.TaskSortDirection
import org.kaorun.kadai.data.model.TaskSortField
import org.kaorun.kadai.ui.icons.arrow_back
import org.kaorun.kadai.ui.icons.arrow_downward_alt
import org.kaorun.kadai.ui.icons.arrow_upward_alt
import org.kaorun.kadai.ui.icons.close
import org.kaorun.kadai.ui.icons.menu
import org.kaorun.kadai.ui.icons.swap_vert

@Composable
fun TopAppBar(
    navigationRailState: WideNavigationRailState,
    sortConfig: TaskSortConfig,
    onSortByClick: (TaskSortField) -> Unit,
    onSearch: (String) -> Unit,
    recentSearches: List<String> = emptyList(),
    onSaveRecentSearch: (String) -> Unit = {},
    onDeleteRecentSearch: (String) -> Unit = {},
    onRecentSearchFilterChange: (String) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val searchBarState = rememberContainedSearchBarState()
    val textFieldState = rememberTextFieldState()
    var sortMenuExpanded by rememberSaveable { mutableStateOf(false) }

    // Fix: on activity/config recreation (e.g. rotation), Android can restore
    // focus onto the search input field, which drives the search bar into the
    // Expanded state on its own. Force-clear focus and collapse on first
    // composition to prevent the search bar from auto-opening.
    LaunchedEffect(Unit) {
        if (searchBarState.currentValue == SearchBarValue.Collapsed) {
            focusManager.clearFocus(force = true)
            scope.launch { searchBarState.animateToCollapsed() }
        }
    }

    val isSearchExpanded = searchBarState.targetValue == SearchBarValue.Expanded ||
        searchBarState.currentValue == SearchBarValue.Expanded

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .collect { query ->
                onRecentSearchFilterChange(query)
            }
    }

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            onSearch = {
                val query = textFieldState.text.toString().trim()
                textFieldState.setTextAndPlaceCursorAtEnd(query)
                if (query.isNotBlank()) {
                    onSaveRecentSearch(query)
                }
                onSearch(query)
                focusManager.clearFocus()
                scope.launch { searchBarState.animateToCollapsed() }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                val isExpanded = searchBarState.targetValue == SearchBarValue.Expanded
                val alignmentBias by animateFloatAsState(
                    targetValue = if (isExpanded) -1f else 0f,
                    animationSpec = motionScheme.fastSpatialSpec()
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = BiasAlignment(
                        horizontalBias = alignmentBias,
                        verticalBias = 0f
                    )
                ) {
                    Text(
                        modifier = Modifier.clearAndSetSemantics { },
                        text = stringResource(R.string.search)
                    )
                }
            },
            leadingIcon = {
                SearchBarIcon(
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd("")
                        onSearch("")
                        focusManager.clearFocus()
                        scope.launch { searchBarState.animateToCollapsed() }
                    },
                    imageVector = arrow_back,
                    contentDescription = stringResource(R.string.back),
                    isVisible = isSearchExpanded || textFieldState.text.isNotBlank(),
                )
            },
            trailingIcon = {
                SearchBarIcon(
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd("")
                        onSearch("")
                    },
                    imageVector = close,
                    contentDescription = stringResource(R.string.clear),
                    isVisible = isSearchExpanded && textFieldState.text.isNotBlank()
                )
            },
            colors = appBarWithSearchColors().searchBarColors.inputFieldColors
        )
    }

    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val isWideScreen =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    Surface(
        color = colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        AppBarWithSearch(
            state = searchBarState,
            inputField = inputField,
            navigationIcon = {
                TopAppBarIconButton(
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
                Box {
                    TopAppBarIconButton(
                        onClick = { sortMenuExpanded = true },
                        imageVector = swap_vert,
                        contentDescription = stringResource(R.string.sort_by),
                        isStart = false,
                        isVisible = searchBarState.targetValue == SearchBarValue.Collapsed
                    )

                    SortMenu(
                        expanded = sortMenuExpanded,
                        sortConfig = sortConfig,
                        onSortByClick = onSortByClick,
                        onDismissRequest = { sortMenuExpanded = false }
                    )
                }
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
            windowInsets = if (isWideScreen) {
                WindowInsets.safeDrawing.only(WindowInsetsSides.Start + WindowInsetsSides.Top)
            } else {
                SearchBarDefaults.windowInsets
            }
        )

        if (isSearchExpanded) {
            ExpandedFullScreenContainedSearchBar(
                state = searchBarState,
                inputField = inputField,
                colors = appBarWithSearchColors().searchBarColors
            ) {
                HorizontalDivider()

                RecentSearchesList(
                    recentSearches = recentSearches,
                    textFieldState = textFieldState,
                    scope = scope,
                    searchBarState = searchBarState,
                    onSearch = onSearch,
                    onSaveRecentSearch = onSaveRecentSearch,
                    onDeleteRecentSearch = onDeleteRecentSearch
                )
            }
        }
    }
}

@Composable
private fun SortMenu(
    expanded: Boolean,
    sortConfig: TaskSortConfig,
    onSortByClick: (TaskSortField) -> Unit,
    onDismissRequest: () -> Unit
) {
    val dateModifiedString = stringResource(R.string.date_modified)
    val dateCreatedString = stringResource(R.string.date_created)
    val dateReminderString = stringResource(R.string.date_reminder)
    val titleString = stringResource(R.string.title)
    val scope = rememberCoroutineScope()
    val sortOptions = remember(dateCreatedString, dateReminderString, titleString) {
        listOf(
            TaskSortField.DATE_MODIFIED to dateModifiedString,
            TaskSortField.DATE_CREATED to dateCreatedString,
            TaskSortField.DATE_REMINDER to dateReminderString,
            TaskSortField.TITLE to titleString
        )
    }

    DropdownMenuPopup(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuGroup(
            modifier = Modifier.width(IntrinsicSize.Max),
            shapes = MenuDefaults.groupShape(0, 1)
        ) {
            val itemCount = sortOptions.size
            sortOptions.forEachIndexed { itemIndex, (field, label) ->
                val isSelected = sortConfig.field == field
                SelectableDropdownMenuItem(
                    selected = isSelected,
                    onClick = { scope.launch { onSortByClick(field) } },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    text = {
                        Text(text = label)
                    },
                    shapes = MenuDefaults.itemShape(itemIndex, itemCount),
                    trailingContent = if (isSelected) {
                        {
                            val isAsc = sortConfig.direction == TaskSortDirection.ASCENDING
                            val contentDescription = stringResource(
                                if (isAsc) R.string.ascending else R.string.descending
                            )
                            Icon(
                                imageVector = if (isAsc) arrow_upward_alt else arrow_downward_alt,
                                modifier = Modifier.size(MenuDefaults.TrailingIconSize),
                                contentDescription = contentDescription
                            )
                        }
                    } else { { Spacer(modifier = Modifier.width(MenuDefaults.TrailingIconSize)) } }
                )
            }
        }
    }
}

@Composable
private fun SearchBarIcon(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    isVisible: Boolean,
) {
    AnimatedVisibility(
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
}

@Composable
private fun TopAppBarIconButton(
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
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                TooltipAnchorPosition.Below
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
}