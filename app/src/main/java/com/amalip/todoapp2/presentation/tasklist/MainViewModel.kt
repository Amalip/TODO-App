package com.amalip.todoapp2.presentation.tasklist

import com.amalip.todoapp2.core.interactor.UseCase
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

    fun getPendingTasks() {
        getPendingTasks(UseCase.None()) {
            it.fold(::handleFailure) {
                state.value = ListViewState.ReceivedList(it)

                true
            }
        }
    }

    fun getTaskById(taskId: Long) {
        getTaskById(taskId) {
            it.fold(::handleFailure) {
                state.value = ListViewState.ReceivedTask(it)

                true
            }
        }
    }

    fun finishTask(task: Task, position: Int) {
        updateTask(task.apply { status = false }) {
            it.fold(::handleFailure) {
                state.value = ListViewState.TaskFinished(task.id, position)

                true
            }
        }
    }

}