package com.app.taskly.integrations

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.taskly.R
import com.app.taskly.task.data.local.entity.NotificationEntity
import com.app.taskly.task.di.app
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New Token: $token")
        // Send token to your backend if needed
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.notification?.title ?: "No Title"
        val body = remoteMessage.notification?.body ?: "No Body"

        Log.i(TAG, "onMessageReceived: $title -- $body")

        // Show system notification
        sendNotification(title, body)

    }

    fun sendNotification(title: String?, message: String?) {
        val channelId = "taskly_channel"
        val notificationManager = app.applicationContext.getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager

        val channel = NotificationChannel(
            channelId, "Task Notifications", NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Task completion notifications" }

        val notification = NotificationCompat.Builder(app.applicationContext, channelId)
            .setSmallIcon(R.drawable.taskly_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(100, notification)

        // Save to Room DB
        saveNotificationToDatabase(title.toString(), message.toString())
    }

    private fun saveNotificationToDatabase(title: String, body: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = app.taskDatabase.notificationDao()
            val notification = NotificationEntity(
                title = title,
                message = body,
                timestamp = System.currentTimeMillis()
            )
            dao.insertNotification(notification)
        }
    }
}
