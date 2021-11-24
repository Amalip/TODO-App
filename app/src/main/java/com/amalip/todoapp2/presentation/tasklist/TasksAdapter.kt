package com.amalip.todoapp2.presentation.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amalip.todoapp2.R
import com.amalip.todoapp2.domain.model.Task
import com.google.android.material.checkbox.MaterialCheckBox
import java.time.format.DateTimeFormatter

/**
 * Created by Amalip on 11/8/2021.
 */

class TasksAdapter(
    private val list: MutableList<Task>,
    var onClickDoneTask: (task: Task, position: Int) -> Unit,
    var onClickDetailTask: (task: Task) -> Unit
) :
    RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    fun add(task: Task) {
        list.add(task)

        notifyItemInserted(list.size - 1)
    }

    fun update(task: Task) {
        val index = list.indexOfFirst { it.id == task.id }
        list[index] = task
        notifyItemChanged(index)
    }

    fun remove(position: Int) {
        list.removeAt(position)

        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class TaskViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Task) = view.apply {
            val txvTitle: TextView = findViewById(R.id.txvTitle)
            val txvDateTime: TextView = findViewById(R.id.txvDateTime)
            val chkFinished: MaterialCheckBox = findViewById(R.id.chkFinished)

            txvTitle.text = data.title
            txvDateTime.text =
                data.dateTime?.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm a"))

            chkFinished.isChecked = false

            chkFinished.setOnClickListener {
                onClickDoneTask(data, adapterPosition)
            }

            rootView.setOnClickListener {
                onClickDetailTask(data)
            }
        }

    }

}