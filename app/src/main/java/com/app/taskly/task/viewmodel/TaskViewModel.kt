package com.app.taskly.task.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.taskly.integrations.MyFirebaseMessagingService
import com.app.taskly.task.data.local.entity.Task
import com.app.taskly.task.data.local.enums.Priority
import com.app.taskly.task.data.local.enums.TaskStatus
import com.app.taskly.task.data.repository.TaskRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val TAG = "taskViewModel"

    val allTasks: Flow<List<Task>> = repository.allTasks

    var simpleTaskName = mutableStateOf("")
    var simpleTaskDate = mutableStateOf("")
    var simpleTaskTime = mutableStateOf("")
    val simpleTaskPriority = mutableStateOf(Priority.Low)

    var recurringTaskName = mutableStateOf("")
    var recurringTaskStartDate = mutableStateOf("")
    var recurringTaskEndDate = mutableStateOf("")
    var recurringTaskTime = mutableStateOf("")
    val recurringTaskPriority = mutableStateOf(Priority.Low)

    private fun initValues() {
        simpleTaskName.value = ""
        simpleTaskDate.value = ""
        simpleTaskTime.value = ""
        simpleTaskPriority.value = Priority.Low

        recurringTaskTime.value = ""
        recurringTaskName.value = ""
        recurringTaskPriority.value = Priority.Low
        recurringTaskStartDate.value = ""
        recurringTaskEndDate.value = ""
    }

    fun addTask(task: Task) = viewModelScope.launch {
        repository.addTask(task)
        initValues()
        saveTaskToFirebase(task)
    }

    private fun saveTaskToFirebase(task: Task) {
        Log.i(TAG, "saveTaskToFirebase: ")
        val db = Firebase.firestore
        db.collection("tasks").document(task.id.toString())
            .set(task)
            .addOnSuccessListener { Log.d("Firestore", "Task added successfully") }
            .addOnFailureListener { Log.e("Firestore", "Error adding task", it) }
    }


    fun addRecurringTasks() {
        val startDateStr = recurringTaskStartDate.value
        val endDateStr = recurringTaskEndDate.value
        val taskName = recurringTaskName.value
        val taskTime = recurringTaskTime.value
        val taskPriority = recurringTaskPriority.value

        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH)

        val startDate = LocalDate.parse(startDateStr, formatter)
        val endDate = LocalDate.parse(endDateStr, formatter)
        val daysBetween = ChronoUnit.DAYS.between(startDate, endDate)

        for (i in 0..daysBetween) {
            val taskDate = startDate.plusDays(i).format(formatter) // Format back to required format

            addTask(
                Task(
                    title = taskName,
                    date = taskDate,
                    startDate = startDateStr,
                    endDate = endDateStr,
                    time = taskTime,
                    status = TaskStatus.Todo,
                    priority = taskPriority
                )
            )
        }
    }


    fun updateTask(task: Task, taskStatus: TaskStatus) = viewModelScope.launch {
        repository.updateTask(task.copy(status = taskStatus))
        if(taskStatus == TaskStatus.Completed) {
            sendTaskCompletionNotification(task.title)
        }
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun deleteAllTasks() = viewModelScope.launch {
        repository.deleteAllTasks()
    }

    fun checkForSimpleTask(): Boolean {
        return simpleTaskName.value.isNotEmpty() && simpleTaskDate.value.isNotEmpty() && simpleTaskTime.value.isNotEmpty()
    }

    fun checkForRecurringTask(): Boolean {
        return recurringTaskName.value.isNotEmpty() && recurringTaskStartDate.value.isNotEmpty() && recurringTaskEndDate.value.isNotEmpty() && recurringTaskTime.value.isNotEmpty()
    }

    private fun sendTaskCompletionNotification(taskTitle: String) {
        MyFirebaseMessagingService().sendNotification(
            title = "Task Completed",
            message = "You completed: $taskTitle"
        )
    }
}
