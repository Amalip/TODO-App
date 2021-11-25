package com.amalip.todoapp2.presentation.form

import com.amalip.todoapp2.core.presentation.BaseViewModel
import com.amalip.todoapp2.domain.model.Task
import com.amalip.todoapp2.domain.usecase.SaveNewTask
import com.amalip.todoapp2.domain.usecase.UpdateTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Inject

/**
 * Created by Amalip on 11/23/2021.
 */

@DelicateCoroutinesApi
@HiltViewModel
class FormViewModel @Inject constructor(
    private val saveNewTask: SaveNewTask,
    private val updateTask: UpdateTask
) : BaseViewModel() {

    fun saveNewTask(task: Task) {
        saveNewTask(task) {
            it.fold(::handleFailure) {
                setNotificationFor(task.apply { id = it })

                true
            }
        }
    }

    fun updateTask(task: Task) {
        updateTask(task) {
            it.fold(::handleFailure) {
                setNotificationFor(task)

                true
            }
        }
    }

    private fun setNotificationFor(task: Task) {
        state.value = FormViewState.ScheduleNotificationFor(task)
        state.value = FormViewState.Success
    }

}