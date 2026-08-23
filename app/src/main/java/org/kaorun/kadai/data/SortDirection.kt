package org.kaorun.kadai.data

enum class SortDirection {
    ASCENDING,
    DESCENDING;

    fun toggle(): SortDirection = when (this) {
        ASCENDING -> DESCENDING
        DESCENDING -> ASCENDING
    }
}