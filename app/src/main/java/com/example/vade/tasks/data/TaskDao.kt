package com.example.vade.tasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC, dueTime ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE dueDate = :date ORDER BY dueTime ASC, priority DESC")
    fun getTasksForDate(date: LocalDate): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isTop3 = 1 AND isCompleted = 0 ORDER BY priority DESC")
    fun getTop3Tasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
    
    @Query("SELECT COUNT(*) FROM tasks WHERE isTop3 = 1 AND isCompleted = 0")
    suspend fun getTop3Count(): Int
}
