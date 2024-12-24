package com.lds.appupdater

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun UpdateDialog(
    onUpdateClick: () -> Unit,
    onDismiss: () -> Unit,
    isUpdating: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Доступно обновление") },
        text = {
            if (isUpdating) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.align(Alignment.Center)// Центрируем
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Загрузка обновления...")
                    CircularProgressIndicator()
                }
            } else {
                Text("Доступна новая версия приложения. Хотите обновить?")
            }
        },
        confirmButton = {
            if (!isUpdating) {
                TextButton(onClick = onUpdateClick) {
                    Text("Обновить")
                }
            }
        },
        dismissButton = {
            if (!isUpdating) {
                TextButton(onClick = onDismiss) {
                    Text("Отмена")
                }
            }
        }
    )
}
