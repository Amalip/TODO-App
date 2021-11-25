package com.amalip.todoapp2.data.dao

import androidx.room.*
import com.amalip.todoapp2.domain.model.Task

/**
 * Created by Amalip on 11/23/2021.
 */

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task WHERE status = 1")
    fun getPendingTasks(): List<Task>

    @Query("SELECT * FROM Task WHERE status = 1 AND id = :id")
    fun getTaskBy(id: Long): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNewTask(task: Task): Long

    @Update
    fun updateTask(task: Task): Int

}