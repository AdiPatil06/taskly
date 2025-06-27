package com.app.taskly.task.di

import android.app.Application
import com.app.taskly.shared.Datastore
import com.app.taskly.shared.SharedViewModel
import com.app.taskly.task.data.local.constants.Constants
import com.app.taskly.task.data.local.dao.TaskDao
import com.app.taskly.task.data.local.database.TaskDatabase
import com.app.taskly.task.data.repository.TaskRepository

lateinit var app: AppModule

class AppModule : Application() {

    lateinit var taskDatabase: TaskDatabase
    lateinit var taskDao: TaskDao
    lateinit var taskRepository: TaskRepository

    val constants = Constants()

    val datastore : Datastore by lazy {
        Datastore(app.applicationContext)
    }

    val sharedViewModel : SharedViewModel by lazy {
        SharedViewModel()
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        taskDatabase = TaskDatabase.getDatabase()

        taskDao = taskDatabase.taskDao()
        taskRepository = TaskRepository(taskDao)
    }
}
