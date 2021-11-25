package com.amalip.todoapp2.presentation.tasklist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.amalip.todoapp2.R
import com.amalip.todoapp2.core.enums.TaskStatus.*
import com.amalip.todoapp2.core.extension.failure
import com.amalip.todoapp2.core.extension.observe
import com.amalip.todoapp2.core.presentation.BaseActivity
import com.amalip.todoapp2.core.presentation.BaseViewState
import com.amalip.todoapp2.domain.model.Task
import com.amalip.todoapp2.presentation.form.FormActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

@DelicateCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var rcvTasks: RecyclerView
    private lateinit var btnAddTask: FloatingActionButton

    private lateinit var adapter: TasksAdapter

    private var clickedNotificationTaskId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel.apply {
            observe(state, ::onViewStateChanged)
            failure(failure, ::handleFailure)

            getPendingTasks()
        }

        clickedNotificationTaskId = intent.getLongExtra("notificationID", -1)
        if (clickedNotificationTaskId != -1L)
            mainViewModel.getTaskById(clickedNotificationTaskId)

        initViews()
    }

    override fun onViewStateChanged(state: BaseViewState?) {
        when (state) {
            is ListViewState.ReceivedList -> {
                setAdapter(state.tasks.toMutableList())
            }
            is ListViewState.ReceivedTask -> {
                initDetailsTask(state.task)
            }
            is ListViewState.TaskFinished -> {
                adapter.remove(state.position)
                cancelScheduleNotification("NOTIFICATION_WORK ${state.taskId}")
            }
        }
    }

    private fun initViews() {
        rcvTasks = findViewById(R.id.rcvTasks)
        btnAddTask = findViewById(R.id.btnAddTask)

        btnAddTask.setOnClickListener {
            startActivityForResult(Intent(this, FormActivity::class.java), NEW_TASK.code)
        }
    }

    private fun initDetailsTask(task: Task) {
        startActivityForResult(Intent(this, FormActivity::class.java).apply {
            putExtra("isTaskDetail", true)
            putExtra("task", task)
        }, UPDATE_TASK.code)
    }

    private fun setAdapter(tasks: MutableList<Task>) {
        adapter = TasksAdapter(tasks, onClickDoneTask = { task, position ->
            /*MainScope().launch {
                db.taskDao().updateTask(task.apply {
                    status = false
                })

                adapter.remove(position)

                cancelScheduleNotification("NOTIFICATION_WORK ${task.id}")
            }*/
            mainViewModel.finishTask(task, position)
        }, onClickDetailTask = { task ->
            initDetailsTask(task)
        })

        rcvTasks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcvTasks.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_TASK.code || requestCode == UPDATE_TASK.code)
            mainViewModel.getPendingTasks()
    }

    private fun cancelScheduleNotification(uniqueWorkName: String) {
        WorkManager.getInstance(this).cancelUniqueWork(uniqueWorkName)
    }

}