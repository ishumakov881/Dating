package com.lds.quickdeal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lds.quickdeal.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    var megaplanUsername by remember { mutableStateOf("") }
    var megaplanPassword by remember { mutableStateOf("") }

    var accessToken by remember { mutableStateOf("") }
    var expiresIn by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Получаем данные из ViewModel (если они есть)
    LaunchedEffect(Unit) {
        settingsViewModel.getSettingsData(
            onSuccess = { settings ->

                megaplanUsername = settings.megaplanUsername ?: ""
                megaplanPassword = settings.megaplanPassword ?: ""

                accessToken = settings.accessToken ?: ""
                expiresIn = settings.expiresIn ?: ""
            },
            onError = { error ->
                Toast.makeText(context, "Ошибка при загрузке настроек: $error", Toast.LENGTH_LONG)
                    .show()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Настройки", style = MaterialTheme.typography.headlineLarge)

        // Поле для ввода логина Мегаплан
        OutlinedTextField(
            value = megaplanUsername,
            onValueChange = { megaplanUsername = it },
            label = { Text("Мегаплан username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Поле для ввода пароля Мегаплан
        OutlinedTextField(
            value = megaplanPassword,
            onValueChange = { megaplanPassword = it },
            label = { Text("Мегаплан password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Поле для отображения access_token
        OutlinedTextField(
            value = accessToken,
            onValueChange = {},
            label = { Text("access_token") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Поле для отображения expires_in
        OutlinedTextField(
            value = expiresIn,
            onValueChange = {},
            label = { Text("expires_in") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Кнопка для сохранения данных
        Button(
            onClick = {
                settingsViewModel.saveSettings(
                    megaplanUsername,
                    megaplanPassword,
                    { success ->
                        Toast.makeText(context, "Настройки сохранены", Toast.LENGTH_SHORT).show()
                    },
                    { error ->
                        Toast.makeText(
                            context,
                            "Ошибка сохранения настроек: $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.End)
        ) {
            Text("Сохранить настройки")
        }
    }

}