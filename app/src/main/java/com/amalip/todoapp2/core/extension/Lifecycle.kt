package com.amalip.todoapp2.core.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.amalip.todoapp2.core.exception.Failure

/**
 * Created by Amalip on 8/21/2021.
 */

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <L : LiveData<Failure>> LifecycleOwner.failure(liveData: L, body: (Failure?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <T> LifecycleOwner.removeObservers(liveDataList: List<T>) =
    liveDataList.forEach { (it as LiveData<*>).removeObservers(this) }