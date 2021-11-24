package com.amalip.todoapp2.presentation.tasklist

import com.amalip.todoapp2.core.presentation.BaseViewModel
import com.amalip.todoapp2.domain.model.Task
import com.amalip.todoapp2.domain.usecase.GetPendingTasks
import com.amalip.todoapp2.domain.usecase.GetTaskById
import com.amalip.todoapp2.domain.usecase.UpdateTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Inject

/**
 * Created by Amalip on 11/23/2021.
 */

@DelicateCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPendingTasks: GetPendingTasks,
    private val getTaskById: GetTaskById,
    private val updateTask: UpdateTask
) : BaseViewModel() {

    fun getPendingTasks() = getPendingTasks

    fun getTaskById(taskId: Long) = getTaskById

    fun finishTask(task: Task, position: Int) = updateTask

    fun changeState() {
        state.value = ListViewState.Test
    }

}