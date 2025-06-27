package com.app.taskly.task.data.local.database

import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.app.taskly.task.data.local.dao.NotificationDao
import com.app.taskly.task.data.local.dao.TaskDao
import com.app.taskly.task.data.local.entity.NotificationEntity
import com.app.taskly.task.data.local.entity.Task
import com.app.taskly.task.di.app

@Database(entities = [Task::class, NotificationEntity::class], version = 7, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getTaskDatabase(): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    app.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
