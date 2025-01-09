package com.lds.quickdeal.ui.tasks

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.megaplan.entity.TaskStatus


@Composable
fun TaskStatusIndicator(context: Context, task: UploaderTask) {
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
            tint = Color.Magenta
        }

        TaskStatus.REACHED_MEGA_PLAN -> {
            contentDescription = "Дошла в мегаплан"
            imageVector = Icons.Filled.CheckCircle
            toastMessage = "Задача в мегаплане секретаря"
            tint = Color.Green
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
            .padding(end = 16.dp)
            .clickable {
                Toast
                    .makeText(context, toastMessage, Toast.LENGTH_SHORT)
                    .show()
            }
    )
}
