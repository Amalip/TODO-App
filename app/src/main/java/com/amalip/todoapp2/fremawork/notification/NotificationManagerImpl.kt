package com.amalip.todoapp2.fremawork.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.amalip.todoapp2.presentation.tasklist.MainActivity
import com.amalip.todoapp2.R
import com.amalip.todoapp2.domain.model.Task

/**
 * Created by Amalip on 11/17/2021.
 */

class NotificationManagerImpl(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    override fun doWork(): Result {
        val id = inputData.getLong("notificationID", 0)
        val title = inputData.getString("notificationTitle") ?: ""
        val description = inputData.getString("notificationDescription") ?: ""

        createNotification(Task(id, title, description))

        return success()
    }

    private fun createNotification(task: Task) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notificationID", task.id)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, "TASK_CHANNEL")
            .setSmallIcon(R.drawable.ic_clock)
            .setContentTitle(task.title)
            .setContentText(task.description)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(task.id.toInt(), builder.build())
        }

    }

}