package com.example.vade.tasks.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getTasksForDate(date: LocalDate): Flow<List<Task>>
    fun getTop3Tasks(): Flow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun canAddToTop3(): Boolean
}

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    override fun getTasksForDate(date: LocalDate): Flow<List<Task>> = taskDao.getTasksForDate(date)
    override fun getTop3Tasks(): Flow<List<Task>> = taskDao.getTop3Tasks()
    override suspend fun addTask(task: Task) = taskDao.insertTask(task)
    override suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    override suspend fun canAddToTop3(): Boolean = taskDao.getTop3Count() < 3
}
