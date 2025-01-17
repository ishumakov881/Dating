package com.lds.quickdeal.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.android.utils.TimeUtils
import com.lds.quickdeal.android.utils.UIUtils
import com.lds.quickdeal.megaplan.entity.TaskStatus
import androidx.compose.material3.IconButton as IconButton1


@Composable
fun TaskCard(
    task: UploaderTask, onClick: (task: UploaderTask) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick(task) }, elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Заголовок: Название задачи и статус
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                TaskStatusIndicator(context, task)

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

//                if (task.megaplanId.isNotEmpty()) {
//                    IconButton1(onClick = { /* Открыть действия */ }) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert, contentDescription = "Действия"
//                        )
//                    }
//                }

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

                            IconButton1(onClick = { menuExpanded = true }) {
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

            // Описание
            if (task.subject.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.subject,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Ответственный и владелец
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                task.owner?.let {
                    UserAvatar(
                        imageUrl = UIUtils.getAvatarUrl(task.owner.avatar?.thumbnail ?: ""),
                        placeholder = Icons.Filled.Person,
                        modifier = Modifier
                            .width(36.dp)
                            .height(36.dp),
                        isOnline = task.owner.isOnline
                    )
                }

                task.responsible?.let {
                    UserAvatar(
                        imageUrl = UIUtils.getAvatarUrl(task.responsible.avatar?.thumbnail ?: ""),
                        placeholder = Icons.Filled.Person,
                        modifier = Modifier
                            .width(36.dp)
                            .height(36.dp)
                            .offset(x = (-12).dp),
                        isOnline = it.isOnline
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Владелец: ${task.owner?.name.orEmpty()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Ответственный: ${task.responsible?.name.orEmpty()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Время создания и изменения
            Spacer(modifier = Modifier.height(8.dp))
            if (task.createdAt.isNotEmpty()) {
                Text(
                    text = "Создана: ${TimeUtils.formatDate(task.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (task.updatedAt.isNotEmpty()) {
                Text(
                    text = "Обновлена: ${TimeUtils.formatDate(task.updatedAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun UserAvatar(
    imageUrl: String?,
    placeholder: ImageVector,
    isOnline: Boolean, // Новый параметр для статуса
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant, CircleShape)
    ) {

        //println("AsyncImage ${imageUrl}")

        if (!imageUrl.isNullOrEmpty()) {

            AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
                .crossfade(true).build(),
                contentDescription = "Аватар",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                onError = { error ->
                    println("AsyncImage | Ошибка загрузки аватара для ${imageUrl}: ${error.result.throwable.message}")
                })
        } else {
            Icon(
                imageVector = placeholder,
                contentDescription = "Пустая аватарка",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Center)
            )
        }

//        // Индикатор статуса
//        Box(
//            modifier = Modifier
//                .padding(bottom = 4.dp)
//                .padding(end = 4.dp)
//                .width(8.dp)
//                .height(8.dp)
//
//                .align(Alignment.BottomEnd)
//                .clip(CircleShape)
//                .background(if (isOnline) Color.Green else Color.Red)
//                .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
//        )
    }
}
