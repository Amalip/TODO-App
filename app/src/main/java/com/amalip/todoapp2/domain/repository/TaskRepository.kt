package com.amalip.todoapp2.domain.repository

import com.amalip.todoapp2.domain.model.Task

/**
 * Created by Amalip on 11/23/2021.
 */

interface TaskRepository {

    fun getPendingTasks(): List<Task>

    fun getTaskById(taskId: Long): Task?

    fun saveNewTask(task: Task): Long

    fun updateTask(task: Task): Int

}