package com.app.taskly.task.data.repository

import com.app.taskly.task.data.local.dao.NotificationDao
import com.app.taskly.task.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val notificationDao: NotificationDao) {

    val allNotifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()

    suspend fun addNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    suspend fun deleteAllNotifications() {
        notificationDao.clearNotifications()
    }

    suspend fun deleteNotification(notification: NotificationEntity) {
        notificationDao.deleteNotification(notification)
    }
}
