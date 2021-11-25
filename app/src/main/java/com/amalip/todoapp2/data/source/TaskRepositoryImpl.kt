package com.amalip.todoapp2.data.source

import com.amalip.todoapp2.data.dao.TaskDao
import com.amalip.todoapp2.domain.model.Task
import com.amalip.todoapp2.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Created by Amalip on 11/23/2021.
 */

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao): TaskRepository {

    override fun getPendingTasks() = taskDao.getPendingTasks()

    override fun getTaskById(taskId: Long) = taskDao.getTaskBy(taskId)

    override fun saveNewTask(task: Task) = taskDao.saveNewTask(task)

    override fun updateTask(task: Task) = taskDao.updateTask(task)

}