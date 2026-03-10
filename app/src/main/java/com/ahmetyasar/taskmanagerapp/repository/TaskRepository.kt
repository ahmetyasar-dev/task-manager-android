package com.ahmetyasar.taskmanagerapp.repository

import androidx.lifecycle.LiveData
import com.ahmetyasar.taskmanagerapp.data.local.dao.TaskDao
import com.ahmetyasar.taskmanagerapp.data.local.entity.Task

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}