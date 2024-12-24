package com.lds.quickdeal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sort


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lds.quickdeal.R
import com.lds.quickdeal.ui.tasks.TaskItem
import com.lds.quickdeal.ui.viewmodels.TaskListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    navController: NavController,
    viewModel: TaskListViewModel = hiltViewModel()
) {

    var context = LocalContext.current

    val tasks by viewModel.tasks.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    var showSortMenu by remember { mutableStateOf(false) } // Состояние для меню сортировки
    var selectedSortField by remember { mutableStateOf("createdAt") } // Поле для сортировки

    // Состояние для текста диалога
    var dialogMessage by remember { mutableStateOf("Вы уверены, что хотите очистить все задачи?") }


    LaunchedEffect(key1 = tasks) {
        // Если задача обновляется или мы возвращаемся на экран,
        // перезагружаем список задач.
        viewModel.loadTasks()
    }

    Scaffold(
        topBar = {
            TopAppBar(

                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть"
                        )
                    }
                },

                title = { Text("Список задач") },
                actions = {

                    //FILTER
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = "Сортировать")
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                selectedSortField = "createdAt"
                                showSortMenu = false
                                viewModel.sortTasks(selectedSortField)
                            },
                            text = { Text("По дате создания") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                selectedSortField = "name"
                                showSortMenu = false
                                viewModel.sortTasks(selectedSortField)
                            },
                            text = { Text("По теме") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                selectedSortField = "subject"
                                showSortMenu = false
                                viewModel.sortTasks(selectedSortField)
                            },
                            text = { Text("По содержимому") }
                        )


                        //|++++++++++++++++++
//                        DropdownMenuItem(
//                            onClick = {
//                                viewModel.filterTasksByStatus(true)
//                                showSortMenu = false
//                            },
//                            text = { Text("Выполненные") }
//                        )
//                        DropdownMenuItem(
//                            onClick = {
//                                viewModel.filterTasksByStatus(false)
//                                showSortMenu = false
//                            },
//                            text = { Text("Невыполненные") }
//                        )
//                        DropdownMenuItem(
//                            onClick = {
//                                viewModel.filterTasksByUrgency(true)
//                                showSortMenu = false
//                            },
//                            text = { Text("Срочные") }
//                        )
//                        DropdownMenuItem(
//                            onClick = {
//                                viewModel.filterTasksByUrgency(false)
//                                showSortMenu = false
//                            },
//                            text = { Text("Несрочные") }
//                        )
                        //|++++++++++++++++++


                    }
                    //END_FILTER


                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Очистить базу"
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    navController.navigate("form") {
                        popUpTo(0) { inclusive = true } // Очищает стек полностью
                        launchSingleTop = true
                    }

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Создать задачу"
                )
            }
        }

    ) { paddingValues ->


        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Подтверждение") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearTasks()
                            showDialog = false
                        }
                    ) {
                        Text("Очистить")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }

        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Задач пока нет", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
            ) {

//                items(tasks) { task ->
//                    TaskItem(navController, task)
//                }

                itemsIndexed(
                    items = tasks,
                    // Provide a unique key based on the email content
                    key = { _, item -> item.hashCode() }
                ) { _, tasksContent ->
                    // Display each email item
                    TaskItem(navController, tasksContent, onRemove = viewModel::removeItem)
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFFFF1744)
        SwipeToDismissBoxValue.EndToStart -> Color(0xFF1DE9B6)
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "delete"
        )
        Spacer(modifier = Modifier)
        Icon(
            // make sure add baseline_archive_24 resource to drawable folder
            painter = painterResource(R.drawable.baseline_archive_24),
            contentDescription = "Archive"
        )
    }
}