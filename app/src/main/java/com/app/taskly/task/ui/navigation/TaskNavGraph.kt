package com.app.taskly.task.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.taskly.task.data.local.entity.Task
import com.app.taskly.task.di.app
import com.app.taskly.task.ui.screen.CalenderScreen
import com.app.taskly.task.ui.screen.MainScreen
import com.app.taskly.task.ui.screen.SplashScreen
import com.app.taskly.task.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

sealed class TaskScreens(val route: String) {
    data object TaskMainScreen : TaskScreens("task_main")
    data object SplashScreen : TaskScreens("splash_screen")
    data object CalenderScreen : TaskScreens("calender_screen")
}

@Composable
fun TaskNavGraph(){
    val navController = rememberNavController()
    val viewModel = TaskViewModel(app.taskRepository)

    val startDestination = if(app.sharedViewModel.showSplashScreen.value == false) {
        TaskScreens.TaskMainScreen.route
    } else TaskScreens.SplashScreen.route

    println("ASP: splashScreen = ${app.sharedViewModel.showSplashScreen.value}")

    if(app.sharedViewModel.showSplashScreen.value != null) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(route = TaskScreens.SplashScreen.route) {
                SplashScreen(
                    onClick = {
                        viewModel.viewModelScope.launch {
                            app.datastore.setShowSplashScreen(false)
                            navController.navigate(
                                TaskScreens.TaskMainScreen.route,
                            )
                        }
                    },
                )
            }

            composable(route = TaskScreens.TaskMainScreen.route) {
                MainScreen(
                    viewModel = viewModel,
                    onClick = {
                        when(it) {
                            "calender" -> navController.navigate(TaskScreens.CalenderScreen.route)
                        }
                    }
                )
            }

            composable(route = TaskScreens.CalenderScreen.route) {
                CalenderScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}