package com.lds.quickdeal.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lds.quickdeal.android.entity.Task
import com.lds.quickdeal.android.utils.TimeUtils


@Composable
fun TaskItem(task: Task) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = task.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                // Индикатор статуса задачи
                if (task.completed) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Завершена",
                        tint = Color.Green
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Pending,
                        contentDescription = "В процессе",
                        tint = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.name,
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


