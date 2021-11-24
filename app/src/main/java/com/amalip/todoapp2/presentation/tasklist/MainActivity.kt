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
import com.amalip.todoapp2.core.enums.TaskStatus.*

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

        createNotificationChannel()

        clickedNotificationTaskId = intent.getLongExtra("notificationID", -1)

        if (clickedNotificationTaskId > -1)
            mainViewModel.getTaskById(clickedNotificationTaskId)

        initViews()
    }

    override fun onViewStateChanged(state: BaseViewState?) {
        when (state) {
            is ListViewState.Test -> {

            }
            is ListViewState.ReceivedList -> {
                setAdapter(state.tasks.toMutableList())
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

    private fun setNotification(task: Task) {
        val zone = OffsetDateTime.now().offset
        val selectedMillis = task.dateTime?.toInstant(zone)?.toEpochMilli() ?: 0
        val nowMillis = LocalDateTime.now().toInstant(zone).toEpochMilli()

        scheduleNotification(
            selectedMillis - nowMillis,
            Data.Builder().apply {
                putLong("notificationID", task.id)
                putString("notificationTitle", task.title)
                putString("notificationDescription", task.description)
            }.build()
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TASKS"
            val descriptionText = "Channel of pending tasks"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("TASK_CHANNEL", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleNotification(delay: Long, data: Data) {

        val notificationWork = OneTimeWorkRequest.Builder(NotificationManagerImpl::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(
            "NOTIFICATION_WORK ${data.getInt("notificationID", 0)}",
            ExistingWorkPolicy.APPEND_OR_REPLACE, notificationWork
        ).enqueue()
    }

    private fun cancelScheduleNotification(uniqueWorkName: String) {
        WorkManager.getInstance(this).cancelUniqueWork(uniqueWorkName)
    }

}