package com.app.taskly

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.app.taskly.task.di.app
import com.app.taskly.task.ui.navigation.TaskNavGraph
import com.app.taskly.task.ui.theme.ToDoAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    
    private val TAG = "MainActivity"

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. You can proceed with sending notifications.
                app.sharedViewModel.setNotificationPermission(true)
            } else {
                // Permission denied. Handle accordingly (e.g., show a message).
                app.sharedViewModel.setNotificationPermission(false)
            }
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "askNotificationPermission: granted")
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // Explain to the user why you need the permission.
                // You can show a dialog or a snackbar.
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Request the permission.
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // Android versions prior to Android 13 don't require runtime notification permissions.
            Log.i(TAG, "askNotificationPermission: does not require")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                Scaffold {
                    Column(modifier = Modifier.padding(it)) {
                        if(!app.sharedViewModel.getNotificationPermission()) {
                            askNotificationPermission()
                        }
                        TaskNavGraph()
                    }
                }
            }
        }
    }
}