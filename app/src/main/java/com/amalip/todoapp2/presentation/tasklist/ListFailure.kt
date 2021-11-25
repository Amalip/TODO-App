package com.amalip.todoapp2.presentation.tasklist

import com.amalip.todoapp2.R
import com.amalip.todoapp2.core.exception.Failure

/**
 * Created by Amalip on 11/23/2021.
 */

sealed class ListFailure {

    object NoInfo : Failure.FeatureFailure(R.string.failure_no_info)

}