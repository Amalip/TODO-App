package com.amalip.todoapp2.presentation.tasklist

import com.amalip.todoapp2.core.presentation.BaseViewState
import com.amalip.todoapp2.domain.model.Task

/**
 * Created by Amalip on 11/23/2021.
 */

sealed class ListViewState: BaseViewState() {

    data class ReceivedList(val tasks: List<Task>): ListViewState()
    data class ReceivedTask(val task: Task) : ListViewState()
    data class TaskFinished(val taskId: Long, val position: Int) : ListViewState()

}