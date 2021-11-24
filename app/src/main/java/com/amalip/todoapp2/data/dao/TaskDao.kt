package com.amalip.todoapp2.data.dao

import androidx.room.*
import com.amalip.todoapp2.domain.model.Task

/**
 * Created by Amalip on 11/23/2021.
 */

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task WHERE status = 1")
    suspend fun getPendingTasks(): List<Task>

    @Query("SELECT * FROM Task WHERE status = 1 AND id = :id")
    suspend fun getTaskBy(id: Long): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

}