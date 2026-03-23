package com.ahmetyasar.taskmanagerapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ahmetyasar.taskmanagerapp.data.local.entity.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE title LIKE :searchQuery OR description LIKE :searchQuery ORDER BY id DESC")
    fun searchTasks(searchQuery: String): LiveData<List<Task>>
}