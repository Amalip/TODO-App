package com.amalip.todoapp2.presentation.form

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.amalip.todoapp2.R
import com.amalip.todoapp2.core.enums.TaskStatus
import com.amalip.todoapp2.core.extension.failure
import com.amalip.todoapp2.core.extension.observe
import com.amalip.todoapp2.core.presentation.BaseActivity
import com.amalip.todoapp2.core.presentation.BaseViewState
import com.amalip.todoapp2.domain.model.Task
import com.amalip.todoapp2.fremawork.notification.NotificationManagerImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

@DelicateCoroutinesApi
@AndroidEntryPoint
class FormActivity : BaseActivity() {

    private val formViewModel: FormViewModel by viewModels()

    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var edtDate: EditText
    private lateinit var edtTime: EditText
    private lateinit var btnAdd: Button

    private var isDetailTask = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        formViewModel.apply {
            observe(state, ::onViewStateChanged)
            failure(failure, ::handleFailure)
        }

        isDetailTask = intent.getBooleanExtra("isTaskDetail", false)

        initViews()
        if (isDetailTask) setTaskInfo(intent.getParcelableExtra("task") ?: Task())
    }

    override fun onViewStateChanged(state: BaseViewState?) {
        when (state) {
            is FormViewState.ScheduleNotificationFor -> {
                setNotification(state.task)
            }
            is FormViewState.Success -> {
                returnToList()
            }
        }
    }

    private fun returnToList() {
        setResult(if (isDetailTask) TaskStatus.UPDATE_TASK.code else TaskStatus.NEW_TASK.code)
        finish()
    }

    private fun setTaskInfo(task: Task) {
        edtTitle.setText(task.title)
        edtDescription.setText(task.description)
        edtDate.setText(task.dateTime?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        edtTime.setText(task.dateTime?.format(DateTimeFormatter.ofPattern("HH:mm")))

        btnAdd.text = "Update"
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        edtDate = findViewById(R.id.edtDate)
        edtTime = findViewById(R.id.edtTime)
        btnAdd = findViewById(R.id.btnAdd)

        edtDate.setOnClickListener {
            val nowDate = LocalDate.now()

            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val realMonth = month + 1
                    edtDate.setText("${if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth}/${if (realMonth < 10) "0$realMonth" else realMonth}/$year")
                },
                nowDate.year,
                nowDate.monthValue - 1,
                nowDate.dayOfMonth
            ).apply {
                datePicker.minDate = Calendar.getInstance().timeInMillis
            }.show()

        }

        edtTime.setOnClickListener {
            val nowTime = LocalTime.now()

            TimePickerDialog(
                this,
                { _, hour, minute ->
                    val realMinute = if (minute < 10) "0$minute" else minute
                    val realHour = if (hour < 10) "0$hour" else hour
                    edtTime.setText("$realHour:$realMinute")
                },
                nowTime.hour,
                nowTime.minute,
                true
            ).show()

        }

        btnAdd.setOnClickListener {
            if (edtTitle.text.isEmpty() || edtDescription.text.isEmpty() || edtDate.text.isEmpty() || edtTime.text.isEmpty()) {
                Toast.makeText(this, "Fill form", Toast.LENGTH_SHORT).show()
            } else {
                val task = Task(
                    intent.getParcelableExtra<Task>("task")?.id ?: 0,
                    edtTitle.text.toString(),
                    edtDescription.text.toString(),
                    LocalDateTime.of(
                        LocalDate.parse(
                            edtDate.text,
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        ),
                        LocalTime.parse(edtTime.text, DateTimeFormatter.ofPattern("HH:mm"))
                    )
                )

                if (isDetailTask) formViewModel.updateTask(task)
                else formViewModel.saveNewTask(task)
            }
        }

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

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotificationManagerImpl::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(
            "NOTIFICATION_WORK ${data.getLong("notificationID", 0)}",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            notificationWork
        ).enqueue()
    }
}