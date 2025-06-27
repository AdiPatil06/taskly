package com.app.taskly.shared

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class Datastore(context: Context) {

    private val TAG = "Datastore"
    private val pref = context.dataStore

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("TASKLY")

        // show splash screen
        private val showSplashScreen = booleanPreferencesKey("SHOW_SPLASH_SCREEN")

        // Notifications permission
        private val notificationPermission = booleanPreferencesKey("notification_permission")
    }

    suspend fun setShowSplashScreen(show: Boolean){
        pref.edit {
            it[showSplashScreen] = show
        }
    }

    fun getShowSplashScreen() = pref.data.map {
        it[showSplashScreen]
    }

    suspend fun setNotificationPermission(show: Boolean) {
        pref.edit {
            it[notificationPermission] = show
        }
    }

    fun getNotificationPermission() = pref.data.map {
        it[notificationPermission]
    }

}