package com.amalip.todoapp2.presentation.form

import com.amalip.todoapp2.core.presentation.BaseViewState
import com.amalip.todoapp2.domain.model.Task

/**
 * Created by Amalip on 11/23/2021.
 */

sealed class FormViewState : BaseViewState() {

    object Success : FormViewState()
    data class ScheduleNotificationFor(val task: Task) : FormViewState()

}