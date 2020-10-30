package com.corvo.demo.db

import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun  addTask(task: TaskDto)

    @Delete
    fun removeTask(id: TaskDto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(task: TaskDto)

    @Query("select * from tasks")
    fun loadTasks(): List<TaskDto>

}