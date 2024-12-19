package com.lds.quickdeal.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.compose.rememberNavController
import com.lds.quickdeal.ui.screens.FormScreen
import com.lds.quickdeal.ui.screens.LoginScreen

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lds.quickdeal.ui.screens.SettingsScreen
import com.lds.quickdeal.ui.screens.TaskListScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

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
            FormScreen(navController = navController, taskId ?: "")
        }

        composable(route = "form") { backStackEntry ->
            FormScreen(navController = navController)
        }

        composable(route = "settings") {
            SettingsScreen(navController = navController)
        }
        composable(route = "tasks") {
            TaskListScreen(navController = navController)
        }

    }

}
