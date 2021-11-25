package com.amalip.todoapp2.presentation.form

import com.amalip.todoapp2.R
import com.amalip.todoapp2.core.exception.Failure

/**
 * Created by Amalip on 11/23/2021.
 */

sealed class FormFailure {

    object CouldNotCreate : Failure.FeatureFailure(R.string.failure_could_not_create)
    object CouldNotUpdate : Failure.FeatureFailure(R.string.failure_could_not_update)

}