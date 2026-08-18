package org.kaorun.kadai.data.repository

import kotlinx.coroutines.flow.Flow
import org.kaorun.kadai.data.Task
import org.kaorun.kadai.data.TaskDao

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllByIdDesc()

    suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getById(taskId)
    }

    suspend fun insert(task: Task): Long {
        return taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }
}