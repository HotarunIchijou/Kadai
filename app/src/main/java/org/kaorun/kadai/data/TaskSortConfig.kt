package org.kaorun.kadai.data

data class TaskSortConfig(
    val field: TaskSortField = TaskSortField.DATE_CREATED,
    val direction: TaskSortDirection = TaskSortDirection.DESCENDING
) {
    fun clickHandler(newField: TaskSortField): TaskSortConfig =
        if (field == newField) {
            copy(direction = direction.toggle())
        } else {
            val defaultDirection = when (newField) {
                TaskSortField.DATE_CREATED -> TaskSortDirection.DESCENDING
                TaskSortField.DATE_REMINDER -> TaskSortDirection.DESCENDING
                TaskSortField.TITLE -> TaskSortDirection.ASCENDING
            }
            TaskSortConfig(field = newField, direction = defaultDirection)
        }
}