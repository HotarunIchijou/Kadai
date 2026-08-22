package org.kaorun.kadai.ui.screens.main.utils

import androidx.compose.foundation.lazy.LazyListState

suspend fun LazyListState.smoothScrollToItem(index: Int) {
    val layout = layoutInfo
    val visibleItem = layout.visibleItemsInfo.firstOrNull { it.index == index }

    val isFullyVisible = visibleItem != null &&
            visibleItem.offset >= layout.viewportStartOffset &&
            (visibleItem.offset + visibleItem.size) <= layout.viewportEndOffset

    if (isFullyVisible) return

    animateScrollToItem(index)
}