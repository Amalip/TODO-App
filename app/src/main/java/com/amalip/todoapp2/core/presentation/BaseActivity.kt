package com.amalip.todoapp2.core.presentation

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amalip.todoapp2.core.exception.Failure

/**
 * Created by Amalip on 9/27/2021.
 */

abstract class BaseActivity : AppCompatActivity(), OnFailure {

    open fun onViewStateChanged(state: BaseViewState?) {

    }

    private fun showToast(message: String) = Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()

    override fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.Unauthorized -> {
                failure.defaultMessage?.let { showToast(getString(it)) }
            }
            is Failure.ServerError -> showToast(failure.serverMessage ?: "")
            is Failure.DatabaseError -> {
                failure.defaultMessage?.let { showToast(getString(it)) }
            }
            is Failure.NetworkConnection -> {
                failure.defaultMessage?.let { showToast(getString(it)) }
            } //showToast(getString(R.string.error_no_internet_connection))
            is Failure.FeatureFailure -> failure.defaultMessage?.let { showToast(getString(it)) }
        }
    }

}