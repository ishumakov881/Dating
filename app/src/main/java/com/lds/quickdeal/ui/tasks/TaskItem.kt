package com.lds.quickdeal.ui.tasks

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lds.quickdeal.android.config.Const

import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.ui.screens.DismissBackground


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: UploaderTask,
    modifier: Modifier = Modifier,
    onRemove: (UploaderTask) -> Unit,
    onClick: (task: UploaderTask) -> Unit

) {

    val context = LocalContext.current
    val currentItem by rememberUpdatedState(task)
    var showDialog by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    showDialog = true
                }

                SwipeToDismissBoxValue.EndToStart -> {
//                    onRemove(currentItem)
//                    Toast.makeText(context, "Item archived", Toast.LENGTH_SHORT).show()
                    showDialog = true
                }

                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState true
            }
            return@rememberSwipeToDismissBoxState true
        },
        // positional threshold of 25%
        positionalThreshold = { it * .25f }
    )






    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Удалить задачу?") },
            text = { Text("Вы уверены, что хотите удалить эту задачу?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemove(currentItem)  // Удаление задачи
                        showDialog = false
                        Toast.makeText(context, "Задача удалена", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    LaunchedEffect(showDialog) {
        if (!showDialog) {
            dismissState.reset()  // Вызываем reset внутри LaunchedEffect
        }
    }

    if (Const.LOCAL_REPO) {
        SwipeToDismissBox(
            state = dismissState,
            modifier = modifier,
            backgroundContent = { DismissBackground(dismissState) },
            content = {
                TaskCard(task, onClick)
            })
    } else {
        TaskCard(task, onClick)
    }
}


fun openMegaplanApp(context: Context, megaplanId: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("megaplan://megaplan/task/$megaplanId/card/")
            `package` = "ru.megaplan.megaplan3app"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Приложение Megaplan не установлено.", Toast.LENGTH_SHORT).show()
    }
}

fun openMegaplanWeb(context: Context, megaplanId: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://megaplan.lds.online/card/$megaplanId")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Приложение не установлено.", Toast.LENGTH_SHORT).show()
    }
}


