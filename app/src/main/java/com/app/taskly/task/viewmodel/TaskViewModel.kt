package com.app.taskly.task.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.taskly.integrations.MyFirebaseMessagingService
import com.app.taskly.task.data.local.entity.NotificationEntity
import com.app.taskly.task.data.local.entity.Task
import com.app.taskly.task.data.local.enums.Priority
import com.app.taskly.task.data.local.enums.TaskStatus
import com.app.taskly.task.data.repository.NotificationRepository
import com.app.taskly.task.data.repository.TaskRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val notificationsRepository: NotificationRepository
) : ViewModel() {

    private val TAG = "taskViewModel"

    val allTasks: Flow<List<Task>> = taskRepository.allTasks

    private val pendingDeleteQueue = MutableStateFlow<List<NotificationEntity>>(emptyList())

    var simpleTaskName = mutableStateOf("")
    var simpleTaskDate = mutableStateOf("")
    var simpleTaskTime = mutableStateOf("")
    val simpleTaskPriority = mutableStateOf(Priority.Low)

    var recurringTaskName = mutableStateOf("")
    var recurringTaskStartDate = mutableStateOf("")
    var recurringTaskEndDate = mutableStateOf("")
    var recurringTaskTime = mutableStateOf("")
    val recurringTaskPriority = mutableStateOf(Priority.Low)

    private val _pendingDeleteQueue = MutableStateFlow<List<NotificationEntity>>(emptyList())

    private val _visibleNotifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val allNotifications: StateFlow<List<NotificationEntity>> = _visibleNotifications

    var countOfNotifications = mutableIntStateOf(0)

    init {
        // Collect original notifications and filter out the pending ones
        viewModelScope.launch {
            notificationsRepository.allNotifications.collect { dbList ->
                val filtered = dbList.filterNot { dbItem ->
                    _pendingDeleteQueue.value.any { it.id == dbItem.id }
                }
                _visibleNotifications.value = filtered
                countOfNotifications.intValue = _visibleNotifications.value.size
            }
        }

        // Background deletion processor
        viewModelScope.launch {
            _pendingDeleteQueue.collect { queue ->
                if (queue.isNotEmpty()) {
                    val toDelete = queue.first()
                    notificationsRepository.deleteNotification(toDelete)
                    delay(200) // optional for smooth batching
                    _pendingDeleteQueue.value = queue.drop(1)
                }
            }
        }
    }

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
        taskRepository.addTask(task)
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
        taskRepository.updateTask(task.copy(status = taskStatus))
        if (taskStatus == TaskStatus.Completed) {
            sendTaskCompletionNotification(task.title)
        }
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskRepository.deleteTask(task)
    }

    fun deleteAllTasks() = viewModelScope.launch {
        taskRepository.deleteAllTasks()
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

    fun requestDelete(notification: NotificationEntity) {
        if (_pendingDeleteQueue.value.none { it.id == notification.id }) {
            _pendingDeleteQueue.value = _pendingDeleteQueue.value + notification
        }
    }
}
