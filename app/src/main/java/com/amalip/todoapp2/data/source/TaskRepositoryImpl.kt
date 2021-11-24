package com.amalip.todoapp2.data.source

import com.amalip.todoapp2.data.dao.TaskDao
import com.amalip.todoapp2.domain.model.Task
import com.amalip.todoapp2.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Created by Amalip on 11/23/2021.
 */

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao): TaskRepository {

    override fun getPendingTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getTaskById(taskId: Long): Task? {
        TODO("Not yet implemented")
    }

    override fun saveNewTask(task: Task): Long {
        TODO("Not yet implemented")
    }

    override fun updateTask(task: Task): Int {
        TODO("Not yet implemented")
    }

}