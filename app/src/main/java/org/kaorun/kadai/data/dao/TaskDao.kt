package org.kaorun.kadai.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import org.kaorun.kadai.data.entity.Task
import org.kaorun.kadai.data.model.TaskSortDirection
import org.kaorun.kadai.data.model.TaskSortField

@Dao
interface TaskDao {
    @RawQuery(observedEntities = [Task::class])
    fun querySorted(query: SupportSQLiteQuery): Flow<List<Task>>

    fun getAll(
        searchQuery: String = "",
        field: TaskSortField,
        direction: TaskSortDirection
    ): Flow<List<Task>> {
        val trimmed = searchQuery.trim()
        val hasSearch = trimmed.isNotEmpty()

        val filter = if (hasSearch) "WHERE (title LIKE ? ESCAPE '\\' OR details LIKE ? ESCAPE '\\')"
        else ""

        val sort = when (field) {
            TaskSortField.DATE_REMINDER ->
                "(${field.column} IS NULL) ASC, ${field.column} ${direction.value}, id ASC"
            TaskSortField.TITLE ->
                "${field.column} COLLATE NOCASE ${direction.value}, id ASC"
            TaskSortField.DATE_CREATED ->
                "${field.column} ${direction.value}, id ASC"
        }

        val query = "SELECT * FROM tasks $filter ORDER BY $sort"

        val bindArgs = if (hasSearch) arrayOf(
            "%${trimmed.escapeLikeWildcards()}%",
            "%${trimmed.escapeLikeWildcards()}%"
        )
        else emptyArray()

        return querySorted(SimpleSQLiteQuery(query, bindArgs))
    }

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getById(taskId: Long): Task?

    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}

private fun String.escapeLikeWildcards(): String =
    replace("\\", "\\\\")
        .replace("%", "\\%")
        .replace("_", "\\_")