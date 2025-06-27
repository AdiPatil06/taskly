package com.app.taskly.shared

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.taskly.task.di.app
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SharedViewModel : ViewModel() {

    private val TAG = "sharedViewModel"

    val showSplashScreen = mutableStateOf<Boolean?>(null)

    init {
        fetchDeviceToken()
        getShowSplashScreen()
        getNotificationPermission()
    }

    private fun getShowSplashScreen() {
        viewModelScope.launch {
            app.datastore.getShowSplashScreen().collectLatest {
                if (it != null) {
                    showSplashScreen.value = it
                } else showSplashScreen.value = true
            }
        }
    }

    private fun fetchDeviceToken() {
        Log.i(TAG, "fetchDeviceToken: entered")
        viewModelScope.launch {
            runCatching {
                FirebaseMessaging.getInstance().token.await()
            }.onSuccess { token ->
                Log.i(TAG, "fetchDeviceToken: $token")
            }.onFailure { exception ->
                Log.e(TAG, "fetchDeviceToken: error", exception)
            }
        }
    }

    fun getNotificationPermission(): Boolean {
        viewModelScope.launch {
            return@launch app.datastore.getNotificationPermission().collect()
        }
        return false
    }

    fun setNotificationPermission(show: Boolean) {
        viewModelScope.launch {
            app.datastore.setNotificationPermission(show)
        }
    }

}