package org.kaorun.kadai.data.model

enum class TaskSortDirection(val value: String) {
    ASCENDING("ASC"),
    DESCENDING("DESC");

    fun toggle(): TaskSortDirection = when (this) {
        ASCENDING -> DESCENDING
        DESCENDING -> ASCENDING
    }
}