package org.kaorun.kadai.data.repository

import kotlinx.coroutines.flow.Flow
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.data.TaskDao
import org.kaorun.kadai.data.TaskSortDirection
import org.kaorun.kadai.data.TaskSortField

class TaskRepository(private val taskDao: TaskDao) {
    fun getAllSorted(
        query: String,
        field: TaskSortField,
        direction: TaskSortDirection
    ): Flow<List<Task>> =
        taskDao.getAll(query, field, direction)

    suspend fun getById(taskId: Long): Task? =
        taskDao.getById(taskId)

    suspend fun insert(task: Task): Long =
        taskDao.insert(task)

    suspend fun update(task: Task) =
        taskDao.update(task)

    suspend fun delete(task: Task) =
        taskDao.delete(task)
}