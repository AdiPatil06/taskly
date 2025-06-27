package com.app.taskly.task.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.taskly.task.data.local.enums.Priority
import com.app.taskly.task.data.local.enums.TaskStatus

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val status: TaskStatus,
    val priority: Priority,
    val startDate: String = "",
    val endDate: String = ""
)