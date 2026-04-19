package com.ahmetyasar.taskmanagerapp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmetyasar.taskmanagerapp.R
import com.ahmetyasar.taskmanagerapp.data.local.entity.Task

class TaskAdapter(
    private val onItemClick: (Task) -> Unit,
    private val onItemLongClick: (Task) -> Unit,
    private val onCheckBoxClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList = emptyList<Task>()

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvTaskDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val checkBoxCompleted: CheckBox = itemView.findViewById(R.id.checkBoxCompleted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position]

        holder.tvTaskTitle.text = currentTask.title
        holder.tvTaskDescription.text = currentTask.description

        holder.checkBoxCompleted.setOnCheckedChangeListener(null)
        holder.checkBoxCompleted.isChecked = currentTask.isCompleted

        val tvTaskTime = holder.itemView.findViewById<TextView>(R.id.tvTaskTime)
        tvTaskTime.text = if (currentTask.time.isNotEmpty()) {
            "Saat: ${currentTask.time}"
        } else {
            "Saat yok"
        }

        if (currentTask.isCompleted) {
            holder.tvTaskTitle.paintFlags =
                holder.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.tvTaskDescription.paintFlags =
                holder.tvTaskDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            holder.tvTaskTitle.alpha = 0.5f
            holder.tvTaskDescription.alpha = 0.5f
        } else {
            holder.tvTaskTitle.paintFlags =
                holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.tvTaskDescription.paintFlags =
                holder.tvTaskDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            holder.tvTaskTitle.alpha = 1.0f
            holder.tvTaskDescription.alpha = 1.0f
        }

        holder.checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
            val updatedTask = currentTask.copy(isCompleted = isChecked)
            onCheckBoxClick(updatedTask)
        }

        holder.itemView.setOnClickListener {
            onItemClick(currentTask)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(currentTask)
            true
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun setTasks(tasks: List<Task>) {
        this.taskList = tasks
        notifyDataSetChanged()
    }
}