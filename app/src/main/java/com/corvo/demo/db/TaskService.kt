package com.corvo.demo.db

interface TaskService {

    fun  addTask(task: TaskDto)

    fun  removeTask(id: Int)

    fun updateTask(task: TaskDto)

}