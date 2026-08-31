package org.kaorun.kadai.data.model

enum class TaskSortField(val column: String) {
    DATE_MODIFIED("modifiedAtTimestamp"),
    DATE_CREATED("createdAtTimestamp"),
    DATE_REMINDER("dueTimestamp"),
    TITLE("title")
}