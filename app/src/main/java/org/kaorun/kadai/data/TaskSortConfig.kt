package org.kaorun.kadai.data

data class TaskSortConfig(
    val sortBy: TaskSortBy = TaskSortBy.DATE_CREATED,
    val direction: SortDirection = SortDirection.DESCENDING
)
