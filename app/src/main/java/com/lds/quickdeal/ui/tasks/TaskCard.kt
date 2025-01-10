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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.android.utils.TimeUtils
import com.lds.quickdeal.megaplan.entity.TaskStatus
import com.lds.quickdeal.ui.viewmodels.TaskViewModel

@Composable
fun TaskCard(
    task: UploaderTask,
    onClick: (task: UploaderTask) -> Unit
) {

    var context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                onClick(task)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                TaskStatusIndicator(context, task)


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


                if (task.status != TaskStatus.REACHED_SERVER && task.status != TaskStatus.NONE) {
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

            var _created = task.createdAt
            if (_created.isNotEmpty()) {
                _created = "Дата создания задачи:               ${TimeUtils.formatDate(task.createdAt)}"
            }

            if (!task.synced.isNullOrEmpty()) {
                _created = "Сохранено на сервере:               ${TimeUtils.formatDate(task.synced)}"
            }


            println("*** $_created | ${task.synced} ${task.localId}")


            if (_created.isNotEmpty()) {
                Text(

                    //text = "Создана: ${TimeUtils.formatDate(it)}",
                    text = _created,

                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Start
                )
            }

            if (task.updatedAt.isNotEmpty()) {
                Text(
                    //text = "Обновлена: ${TimeUtils.formatDate(task.updatedAt)}",
                    text = "Время последнего изменения статуса: ${TimeUtils.formatDate(task.updatedAt)}",

                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Start
                )
            }

        }
    }
}