package com.lds.quickdeal.ui.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.ui.viewmodels.TaskViewModel

@Composable
fun TitileWithPresetes(currentTask: UploaderTask, viewModel: TaskViewModel) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
    ) {
        OutlinedTextField(
            value = currentTask.name,
            onValueChange = { newName -> viewModel.updateTaskName(newName) },
            label = { Text("Тема") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)

            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            minLines = 1,
            singleLine = true
        )

        // Кнопка с меню пресетов
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd) // Размещение кнопки справа
                .padding(top = 8.dp)
        ) {
            var menuExpanded by remember { mutableStateOf(false) }

            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Меню пресетов"
                )
            }


            val FORM_PRESETS_TITLE = listOf(
                "Очистить заголовок" to { viewModel.updateTitle("") },
                "Добавить стандартное приветствие" to { viewModel.appendTitle("Здравствуйте!") },
                "Добавить текущее время" to { viewModel.appendTitle("${getCurrentTime()}") },
            )

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                FORM_PRESETS_TITLE.forEach { (title, action) ->
                    if (title == "Добавить приоритет задачи") {
                        // Подменю для приоритета задачи
                        var priorityMenuExpanded by remember { mutableStateOf(false) }
                        DropdownMenuItem(
                            text = { Text(title) },
                            onClick = { priorityMenuExpanded = !priorityMenuExpanded }
                        )
                        if (priorityMenuExpanded) {
                            // Подменю для выбора приоритета задачи
                            listOf("Высокий", "Средний", "Низкий").forEach { priority ->
                                DropdownMenuItem(
                                    text = { Text(priority) },
                                    onClick = {
                                        viewModel.appendDescription("\n**Приоритет:** $priority\n")
                                        priorityMenuExpanded = false
                                        menuExpanded = false
                                        //Toast.makeText(context, "Приоритет: $priority", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    } else {
                        DropdownMenuItem(
                            text = { Text(title) },
                            onClick = {
                                action()
                                menuExpanded = false
                                //Toast.makeText(context, title, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}