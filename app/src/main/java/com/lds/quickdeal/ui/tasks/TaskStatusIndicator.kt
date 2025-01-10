package com.lds.quickdeal.ui.tasks

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
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

        TaskStatus.CREATED -> {
            contentDescription = "Задача создана"
            imageVector = Icons.Default.AddCircle
            toastMessage = "Создана новая Задача!"
            tint = Color.Blue
        }
        TaskStatus.ASSIGNED -> {
            contentDescription = "Задача назначена исполнителю"
            imageVector = Icons.Default.Person
            toastMessage = "Задача назначена!"
            tint = Color.Yellow
        }
        TaskStatus.ACCEPTED -> {
            contentDescription = "Задача принята"
            imageVector = Icons.Default.Done
            toastMessage = "Задача принята исполнителем!"
            tint = Color.Green
        }
        TaskStatus.DONE -> {
            contentDescription = "Работа по заявке выполнена"
            imageVector = Icons.Default.Work
            toastMessage = "Работа выполнена!"
            tint = Color.Cyan
        }
        TaskStatus.REJECTED -> {
            contentDescription = "Задача отклонена"
            imageVector = Icons.Default.Cancel
            toastMessage = "Задача отклонена!"
            tint = Color.Red
        }
        TaskStatus.CANCELLED -> {
            contentDescription = "Задача отменена"
            imageVector = Icons.Default.Close
            toastMessage = "Задача отменена."
            tint = Color.Gray
        }
        TaskStatus.EXPIRED -> {
            contentDescription = "Срок выполнения заявки истек"
            imageVector = Icons.Default.TimerOff
            toastMessage = "Срок выполнения заявки истек."
            tint = Color.Magenta
        }
        TaskStatus.DELAYED -> {
            contentDescription = "Выполнение задачи задерживается"
            imageVector = Icons.Default.HourglassBottom
            toastMessage = "Задача задерживается!"
            tint = Color(0xFFFFA500)
        }
        TaskStatus.TEMPLATE -> {
            contentDescription = "Шаблон заявки"
            imageVector = Icons.Default.FileCopy
            toastMessage = "Это шаблон заявки."
            tint = Color.LightGray
        }
        TaskStatus.OVERDUE -> {
            contentDescription = "Задача просрочена"
            imageVector = Icons.Default.Warning
            toastMessage = "Задача просрочена!"
            tint = Color.Red
        }

//        else -> {
//            contentDescription = "Неизвестный статус"
//            imageVector = Icons.Default.Help
//            toastMessage = "Неизвестный статус задачи."
//            tint = Color.Gray
//        }
        TaskStatus.NONE -> {}
    }
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint,
        modifier = Modifier
            .padding(end = 16.dp)
            .clickable {
                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            }
    )
}
