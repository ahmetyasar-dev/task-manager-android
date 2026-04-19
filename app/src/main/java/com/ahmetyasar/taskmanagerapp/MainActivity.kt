package com.ahmetyasar.taskmanagerapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetyasar.taskmanagerapp.adapter.TaskAdapter
import com.ahmetyasar.taskmanagerapp.data.local.entity.Task
import com.ahmetyasar.taskmanagerapp.viewmodel.TaskViewModel
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher
import android.app.TimePickerDialog
import java.util.Calendar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etTaskTitle = findViewById<EditText>(R.id.etTaskTitle)
        val etTaskDescription = findViewById<EditText>(R.id.etTaskDescription)
        val btnAddSampleTask = findViewById<Button>(R.id.btnAddSampleTask)
        val recyclerViewTasks = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        val tvEmptyState = findViewById<TextView>(R.id.tvEmptyState)
        val etSearchTask = findViewById<EditText>(R.id.etSearchTask)
        val btnFilterAll = findViewById<Button>(R.id.btnFilterAll)
        val btnFilterActive = findViewById<Button>(R.id.btnFilterActive)
        val btnFilterCompleted = findViewById<Button>(R.id.btnFilterCompleted)
        val btnSelectTime = findViewById<Button>(R.id.btnSelectTime)
        val tvSelectedTime = findViewById<TextView>(R.id.tvSelectedTime)

        btnSelectTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                tvSelectedTime.text = "Seçilen Saat: $selectedTime"
            }, hour, minute, true).show()
        }

        etSearchTask.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()

                if (query.isEmpty()) {
                    taskViewModel.allTasks.observe(this@MainActivity) { tasks ->
                        taskAdapter.setTasks(tasks)

                        if (tasks.isEmpty()) {
                            tvEmptyState.visibility = android.view.View.VISIBLE
                            recyclerViewTasks.visibility = android.view.View.GONE
                        } else {
                            tvEmptyState.visibility = android.view.View.GONE
                            recyclerViewTasks.visibility = android.view.View.VISIBLE
                        }
                    }
                } else {
                    taskViewModel.searchTasks(query).observe(this@MainActivity) { tasks ->
                        taskAdapter.setTasks(tasks)

                        if (tasks.isEmpty()) {
                            tvEmptyState.visibility = android.view.View.VISIBLE
                            recyclerViewTasks.visibility = android.view.View.GONE
                        } else {
                            tvEmptyState.visibility = android.view.View.GONE
                            recyclerViewTasks.visibility = android.view.View.VISIBLE
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        taskAdapter = TaskAdapter(
            onItemClick = { task ->
                showUpdateDialog(task)
            },
            onItemLongClick = { task ->
                taskViewModel.deleteTask(task)
                Toast.makeText(this, "Görev silindi", Toast.LENGTH_SHORT).show()
            },
            onCheckBoxClick = { updatedTask ->
                taskViewModel.updateTask(updatedTask)

                if (updatedTask.isCompleted) {
                    Toast.makeText(this, "Görev tamamlandı", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Görev tekrar aktif", Toast.LENGTH_SHORT).show()
                }
            }
        )

        recyclerViewTasks.adapter = taskAdapter
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        taskViewModel.allTasks.observe(this) { tasks ->
            taskAdapter.setTasks(tasks)

            if (tasks.isEmpty()) {
                tvEmptyState.visibility = android.view.View.VISIBLE
                recyclerViewTasks.visibility = android.view.View.GONE
            } else {
                tvEmptyState.visibility = android.view.View.GONE
                recyclerViewTasks.visibility = android.view.View.VISIBLE
            }
        }
        btnAddSampleTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim()
            val description = etTaskDescription.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(this, "Lütfen görev başlığı gir", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = Task(
                title = title,
                description = description,
                isCompleted = false,
                time = selectedTime
            )

            taskViewModel.insertTask(task)

            Toast.makeText(this, "Görev eklendi", Toast.LENGTH_SHORT).show()

            etTaskTitle.text.clear()
            etTaskDescription.text.clear()
            selectedTime = ""
            tvSelectedTime.text = "Saat seçilmedi"
        }
        btnFilterAll.setOnClickListener {
            taskViewModel.allTasks.observe(this) { tasks ->
                updateTaskList(tasks, recyclerViewTasks, tvEmptyState)
            }
        }

        btnFilterActive.setOnClickListener {
            taskViewModel.getActiveTasks().observe(this) { tasks ->
                updateTaskList(tasks, recyclerViewTasks, tvEmptyState)
            }
        }

        btnFilterCompleted.setOnClickListener {
            taskViewModel.getCompletedTasks().observe(this) { tasks ->
                updateTaskList(tasks, recyclerViewTasks, tvEmptyState)
            }
        }
    }
    private fun updateTaskList(
        tasks: List<Task>,
        recyclerViewTasks: RecyclerView,
        tvEmptyState: TextView
    ) {
        taskAdapter.setTasks(tasks)

        if (tasks.isEmpty()) {
            tvEmptyState.visibility = android.view.View.VISIBLE
            recyclerViewTasks.visibility = android.view.View.GONE
        } else {
            tvEmptyState.visibility = android.view.View.GONE
            recyclerViewTasks.visibility = android.view.View.VISIBLE
        }
    }

    private fun showUpdateDialog(task: Task) {
        val editTitle = EditText(this)
        val editDescription = EditText(this)

        editTitle.setText(task.title)
        editTitle.hint = "Görev başlığı"

        editDescription.setText(task.description)
        editDescription.hint = "Görev açıklaması"

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        layout.addView(editTitle)
        layout.addView(editDescription)

        AlertDialog.Builder(this)
            .setTitle("Görevi Düzenle")
            .setView(layout)
            .setPositiveButton("Kaydet") { _, _ ->
                val updatedTitle = editTitle.text.toString().trim()
                val updatedDescription = editDescription.text.toString().trim()

                if (updatedTitle.isNotEmpty()) {
                    val updatedTask = task.copy(
                        title = updatedTitle,
                        description = updatedDescription
                    )
                    taskViewModel.updateTask(updatedTask)
                    Toast.makeText(this, "Görev güncellendi", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Başlık boş olamaz", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("İptal", null)
            .show()

    }
}