package com.app.taskly.integrations

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.taskly.R
import com.app.taskly.task.di.app
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New Token: $token")
        // Send token to your backend if needed
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.i(TAG, "onMessageReceived: ${remoteMessage.notification?.title} -- ${remoteMessage.notification?.body}")
        remoteMessage.notification?.let {
            sendNotification(it.title, it.body)
        }
    }

    fun sendNotification(title: String?, message: String?) {
        val channelId = "taskly_channel"
        val notificationManager = app.applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
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
    }
}
