package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.close
import org.kaorun.kadai.ui.icons.schedule
import org.kaorun.kadai.ui.icons.search

@Composable
fun RecentSearchesList(
    recentSearches: List<String>,
    textFieldState: TextFieldState,
    scope: CoroutineScope,
    searchBarState: SearchBarState,
    onSearch: (String) -> Unit,
    onSaveRecentSearch: (String) -> Unit,
    onDeleteRecentSearch: (String) -> Unit
) {
    val cornerSize = 20.dp
    val innerCornerSize = 4.dp
    val colors = ListItemDefaults.colors(containerColor = colorScheme.surfaceBright)

    if (recentSearches.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(
                items = recentSearches,
                key = { _, item -> item }
            ) { index, query ->
                val shape = remember(index, recentSearches.size) {
                    when {
                        recentSearches.size == 1 -> RoundedCornerShape(cornerSize)
                        index == 0 -> RoundedCornerShape(
                            topStart = cornerSize,
                            topEnd = cornerSize,
                            bottomStart = innerCornerSize,
                            bottomEnd = innerCornerSize
                        )

                        index == recentSearches.lastIndex -> RoundedCornerShape(
                            topStart = innerCornerSize,
                            topEnd = innerCornerSize,
                            bottomStart = cornerSize,
                            bottomEnd = cornerSize
                        )

                        else -> RoundedCornerShape(innerCornerSize)
                    }
                }

                SegmentedListItem(
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd(query)
                        onSaveRecentSearch(query)
                        onSearch(query)
                        scope.launch { searchBarState.animateToCollapsed() }
                    },
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = recentSearches.size
                    ),
                    modifier = Modifier
                        .clip(shape)
                        .animateItem(),
                    colors = colors,
                    leadingContent = {
                        Icon(
                            imageVector = schedule,
                            contentDescription = null,
                            tint = colorScheme.onSurfaceVariant
                        )
                    },
                    content = {
                        Text(
                            text = query,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = {
                        IconButton(
                            onClick = { onDeleteRecentSearch(query) }
                        ) {
                            Icon(
                                imageVector = close,
                                contentDescription = stringResource(R.string.delete),
                                tint = colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            RecentSearchesEmpty(
                icon = search,
                title = stringResource(R.string.recent_searches_empty)
            )
        }
    }
}