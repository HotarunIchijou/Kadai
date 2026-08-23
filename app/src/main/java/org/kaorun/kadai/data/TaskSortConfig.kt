package org.kaorun.kadai.data

data class TaskSortConfig(
    val sortBy: TaskSortBy = TaskSortBy.DATE_CREATED,
    val direction: SortDirection = SortDirection.DESCENDING
) {
    fun clickHandler(newSortBy: TaskSortBy): TaskSortConfig {
        return if (sortBy == newSortBy) {
            copy(direction = direction.toggle())
        } else {
            val defaultDirection = when (newSortBy) {
                TaskSortBy.TITLE -> SortDirection.ASCENDING
                TaskSortBy.DATE_CREATED,
                TaskSortBy.DATE_REMINDER -> SortDirection.DESCENDING
            }
            TaskSortConfig(sortBy = newSortBy, direction = defaultDirection)
        }
    }
}