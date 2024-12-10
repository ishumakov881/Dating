package com.lds.quickdeal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FormScreen(navController: NavController) {
    var topic by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Создать заявку", style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text("Тема") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Содержание") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Button(onClick = { /* Открыть камеру или выбрать файл */ }) {
            Text("Добавить фото/видео/файлы")
        }

        Button(onClick = { /* Надиктовать текст */ }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Надиктовать содержание")
        }

        Button(
            onClick = { /* Обработка отправки данных */ },
            modifier = Modifier.padding(top = 16.dp).align(Alignment.End)
        ) {
            Text("Отправить")
        }
    }
}
