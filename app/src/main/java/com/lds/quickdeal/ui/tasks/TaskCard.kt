package com.lds.quickdeal.ui.tasks

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.android.utils.TimeUtils
import com.lds.quickdeal.megaplan.entity.TaskStatus

@Composable
fun TaskCard(
    navController: NavController,
    task: UploaderTask
) {

    var context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                navController.navigate("form/${task._id}")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildAnnotatedString {
                        append(task.name)
                        if (task.megaplanId.isNotEmpty()) {
                            pushStringAnnotation(tag = "megaplan", annotation = task.megaplanId)
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Green,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold

                                )
                            ) {
                                //append(" (${task.megaplanId})")
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Red
                                    )
                                ) {
                                    append(" (${task.megaplanId})")
                                }
                            }
                            pop()
                        }
                    }
                    //text = task.name + if (task.megaplanId.isNotEmpty()) " (${task.megaplanId})" else "",
                    , style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
//                        .clickable {
//                            // Обрабатываем клик на кликабельной части
//                            val megaplanId = task.megaplanId
//                            if (megaplanId.isNotEmpty()) {
//                                // Реализуйте действие для клика на мегаплан ID
//                                println("Clicked on megaplan ID: $megaplanId")
//                                openMegaplanApp(context, megaplanId)
//                            }
//                        }
                )

                // Индикатор статуса задачи

                var contentDescription = ""
                var imageVector = Icons.Filled.CheckCircle
                var tint = Color.Green
                var toastMessage = "none"
                when (task.status) {


                    TaskStatus.REACHED_SERVER -> {
                        contentDescription = "Отправлена на сервер"//В процессе
                        imageVector = Icons.Filled.CheckCircle
                        toastMessage = "Задача: ${task.name} зарегистрирована на сервере!"
                        tint = Color.Gray
                    }

                    TaskStatus.REACHED_MEGA_PLAN -> {
                        contentDescription = "Дошла в мегаплан"
                        imageVector = Icons.Filled.CheckCircle
                        toastMessage = "Задача в мегаплане секретаря"
                        tint = Color.Yellow
                    }

                    TaskStatus.COMPLETED -> {
                        contentDescription = "Завершена"//Выполнена, то есть закрыта кем-то
                        imageVector = Icons.Filled.CheckCircle
                        toastMessage = "Задача завершена!"
                        tint = Color.Green
                    }

                    else -> {

                    }
                }
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = tint,
                    modifier = Modifier
                        .clickable {
                            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                        }
                )


                if (task.status == TaskStatus.COMPLETED) {
                    if (task.megaplanId.isNotEmpty()) {
                        Box {
                            val FORM_PRESETS = listOf(
                                "Посмотреть задачу в Мегаплане App" to {
                                    openMegaplanApp(
                                        context,
                                        task.megaplanId
                                    )
                                },
                                "Посмотреть задачу в Мегаплане Web" to {
                                    openMegaplanWeb(
                                        context,
                                        task.megaplanId
                                    )
                                },

                                )
                            var menuExpanded by remember { mutableStateOf(false) }

                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Меню пресетов"
                                )
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false },
                                modifier = Modifier.align(Alignment.TopEnd) // Выравнивание меню по правому краю
                            ) {
                                FORM_PRESETS.forEach { (title, action) ->
                                    DropdownMenuItem(
                                        text = { Text(title) },
                                        onClick = {
                                            action()
                                            menuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.subject, //task description
                style = MaterialTheme.typography.bodyMedium
            )


            ///
            Spacer(modifier = Modifier.height(8.dp))

            // Отображение времени создания и обновления
            task.createdAt?.let {
                Text(
                    text = "Создана: ${TimeUtils.formatDate(it)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Start
                )
            }

            Text(
                text = "Обновлена: ${TimeUtils.formatDate(task.updatedAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Start
            )
        }
    }
}