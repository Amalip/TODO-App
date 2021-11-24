package com.amalip.todoapp2.core.presentation

import com.amalip.todoapp2.core.exception.Failure

/**
 * Created by Amalip on 9/27/2021.
 */

interface OnFailure {

    fun handleFailure(failure: Failure?)

}