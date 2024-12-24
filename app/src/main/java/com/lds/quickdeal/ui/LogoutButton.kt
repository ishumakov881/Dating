package com.lds.quickdeal.ui

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.lds.quickdeal.ui.form.logout

@Composable
fun LogoutButton(navController: NavController, context: Context) {
    val prefs = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    val dialogShownCountKey = "dialog_shown_count"

    var showDialog by remember { mutableStateOf(false) }

    val dialogShownCount = remember { prefs.getInt(dialogShownCountKey, 0) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Подтверждение выхода") },
            text = { Text("Вы уверены, что хотите выйти?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    // Обновляем счетчик диалогов в SharedPreferences
                    prefs.edit().putInt(dialogShownCountKey, dialogShownCount + 1).apply()

                    // Выполняем логику выхода
                    logout(context)
                    navController.navigate("login") {
                        popUpTo("form") { inclusive = true }
                    }
                }) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    // Кнопка выхода
    IconButton(onClick = {
        if (dialogShownCount < 2) {
            // Показываем диалог только первые два раза
            showDialog = true
        } else {
            // Если диалог уже показан дважды, сразу выполняем выход
            logout(context)
            navController.navigate("login") {
                popUpTo("form") { inclusive = true }
            }
        }
    }) {
        Icon(imageVector = Icons.Filled.Logout, contentDescription = "Выход")
    }
}
