package com.lds.quickdeal.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.compose.rememberNavController
import com.lds.quickdeal.ui.form.FormScreen
import com.lds.quickdeal.ui.screens.LoginScreen

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lds.quickdeal.ui.screens.SettingsScreen
import com.lds.quickdeal.ui.screens.TaskListScreen
import com.lds.quickdeal.ui.viewmodels.TaskViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val sharedTaskViewModel: TaskViewModel = hiltViewModel()

    val handleLogout = {
        sharedTaskViewModel.setTaskForEditing(null)
        navController.navigate("login") {
            popUpTo("form") { inclusive = true }
        }
    }

    val handleBackButton = {
        navController.popBackStack()
        sharedTaskViewModel.setTaskForEditing(null)
    }

    NavHost(
        navController = navController,
        startDestination = "login",

        modifier = Modifier.fillMaxSize()
    ) {

        composable(route = "login") {
            LoginScreen(navController = navController)
        }

        composable(route = "form/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")

            BackHandler {
                handleBackButton()
            }
            FormScreen(
                onBackFromFormScreen = handleBackButton,
                onTaskCreated = {
                    sharedTaskViewModel.setTaskForEditing(null)
                    navController.navigate("tasks")
                },
                onTaskList = {
                    navController.navigate("tasks") // Переход на экран созданных задач
                },
                onLogOut = handleLogout,
                taskId ?: "",
                sharedViewModel = sharedTaskViewModel
            )
        }

        composable(route = "form") { backStackEntry ->
            //...
            FormScreen(
                onBackFromFormScreen = handleBackButton,
                onTaskCreated = {
                    sharedTaskViewModel.setTaskForEditing(null)
                    navController.navigate("tasks")
                },
                onTaskList = {
                    navController.navigate("tasks") // Переход на экран созданных задач
                }, onLogOut = handleLogout,
                sharedViewModel = sharedTaskViewModel
            )
        }

        composable(route = "settings") {
            SettingsScreen(navController = navController)
        }

        composable(route = "tasks") {
            TaskListScreen(

                onBackFromTaskList = {
                    navController.popBackStack()
                },

                onItemClick = { task ->
                    sharedTaskViewModel.setTaskForEditing(task)
                    navController.navigate("form/${task._id}")
                    //navController.navigate("form")
                },


                onCreateNewTask = {
                    sharedTaskViewModel.setTaskForEditing(null)
                    navController.navigate("form") {
                        popUpTo(0) { inclusive = true } // Очищает стек полностью
                        launchSingleTop = true
                    }
                }
            )

        }

    }

}
