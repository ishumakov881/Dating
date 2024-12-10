package com.lds.quickdeal.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.compose.rememberNavController
import com.lds.quickdeal.FormScreen
import com.lds.quickdeal.login.LoginScreen

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


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
        composable(route = "form") {
            FormScreen(navController = navController)
        }
    }
}
