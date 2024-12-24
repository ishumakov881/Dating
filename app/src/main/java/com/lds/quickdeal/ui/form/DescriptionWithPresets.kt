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
fun DescriptionWithPresets(currentTask: UploaderTask, viewModel: TaskViewModel) {
    val FORM_PRESETS = listOf(
        "Очистить содержимое" to { viewModel.updateDescription("") },
        "Добавить стандартное приветствие" to { viewModel.appendDescription("\nЗдравствуйте! \n") },
        "Добавить текущее время" to { viewModel.appendDescription("\n${getCurrentTime()}") },
        //"Добавить шаблон заголовка" to { description += "\n### Заголовок\n" },
        "Добавить разделитель" to { viewModel.appendDescription("\n---\n") },


        //"Добавить приоритет задачи" to { description += "\n**Приоритет:** Высокий\n" },
        "Добавить приоритет задачи" to {},


        "Добавить дедлайн" to { viewModel.appendDescription("\n**Дедлайн:** 31.12.2024\n") },
        "Добавить список подзадач" to { viewModel.appendDescription("\n- Подзадача 1\n- Подзадача 2\n- Подзадача 3\n") },
        //"Добавить описание бизнес-цели" to { description += "\n**Цель:** Увеличить производительность команды.\n" },

        "Добавить шаблон задачи по проекту" to { viewModel.appendDescription("\n**Проект:** Разработка нового продукта\n**Этап:** Исследование\n") },
//                    "Добавить задачи по обучению" to { description += "\n- Прохождение курса по улучшению навыков общения\n- Обучение использованию новых инструментов\n" },
//                    "Добавить статус задачи" to { description += "\n**Статус:** В процессе\n" },
//                    "Добавить шаблон фидбека" to { description += "\n**Фидбек:** Прекрасно выполнена работа, нужно улучшить коммуникацию.\n" }
    )

    //DESCRIPTION


    Box(
        modifier = Modifier.fillMaxWidth()
    ) {


        OutlinedTextField(
            value = currentTask!!.subject,
            onValueChange = { viewModel.updateDescription(it) },
            label = { Text("Содержание") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            minLines = 8,
            singleLine = false
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
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                FORM_PRESETS.forEach { (title, action) ->
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

    //END_DESCRIPTION
}