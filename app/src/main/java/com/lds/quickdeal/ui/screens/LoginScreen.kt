package com.lds.quickdeal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.di.ExportRepositoryEntryPoint
import com.lds.quickdeal.ui.viewmodels.AuthViewModel
import dagger.hilt.android.EntryPointAccessors


@Composable
fun LoginScreen(
    navController: NavController, viewModel: AuthViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf(BuildConfig.ACTIVE_DIRECTORY_USERNAME) }
    var password by remember { mutableStateOf(BuildConfig.ACTIVE_DIRECTORY_PASSWORD) }

    var passwordVisible by remember { mutableStateOf(false) }


    val context = LocalContext.current


    val exportRepository = remember {
        EntryPointAccessors.fromApplication(
            context, ExportRepositoryEntryPoint::class.java
        ).getExportRepository()
    }

    var v0 = exportRepository.getADuserName()
    var v1 = exportRepository.getADPassword()

    if (v0.isNotEmpty() && v1.isNotEmpty()) {

//        username = v0
//        password = v1

        //Try autologin
        viewModel.performAdLogin(context, username, password) { isSuccess ->
            if (isSuccess) {
                navController.navigate("form") {
                    popUpTo("login") { inclusive = true } // Удаляем экран login из стека навигации
                }
            } else {
                //Toast.makeText(context, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    LaunchedEffect(...) {
//        if (....) {
//        Toast.makeText(context, "@@@@@@", Toast.LENGTH_SHORT).show()
//    }
//    }

//    LaunchedEffect(viewModel.accessToken) {
//        if (!viewModel.accessToken.isNullOrEmpty()) {
//            navController.navigate("form") {
//                popUpTo("login") { inclusive = true } // Удаляем экран login из стека навигации
//            }
//        }
//    }

//    LaunchedEffect(viewModel.autologin) {
//        if (viewModel.autologin) {
//            navController.navigate("form") {
//                popUpTo("login") { inclusive = true } // Удаляем экран login из стека навигации
//            }
//        }
//    }

    // Получение версии приложения
    val appVersion = BuildConfig.VERSION_NAME

    // Основной UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Форма авторизации по центру экрана
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center), // Центрируем
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Авторизация", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Логин") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),


                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    // Localized description for accessibility services
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    // Toggle button to hide or display password
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )

            Button(
                onClick = {

                    //Toast.makeText(context, "{$username $password}", Toast.LENGTH_SHORT).show()

                    viewModel.performAdLogin(context, username, password) { isSuccess ->
                        if (isSuccess) {
                            navController.navigate("form") // Навигация при успешной авторизации
                        } else {
                            Toast.makeText(context, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                        }
                    }


                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Войти")
            }
        }

        // Футер с версией приложения прижат к низу
        Text(
            text = "Версия: $appVersion",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}
