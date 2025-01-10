package com.lds.quickdeal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lds.quickdeal.R
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.ui.tasks.TaskItem
import com.lds.quickdeal.ui.viewmodels.TaskListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    onBackFromTaskList: () -> Unit,
    onCreateNewTask: () -> Unit,
    onItemClick: (task: UploaderTask) -> Unit
) {

    var context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    val tasks by viewModel.tasks.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    var showSortMenu by remember { mutableStateOf(false) } // Состояние для меню сортировки
    var selectedSortField by remember { mutableStateOf("createdAt") } // Поле для сортировки

    // Состояние для текста диалога
    var dialogMessage by remember { mutableStateOf("Вы уверены, что хотите очистить все задачи?") }
    val isLoading by viewModel.isLoading.collectAsState()

//    LaunchedEffect(/*key1 = tasks*/Unit) {
//        // Если задача обновляется или мы возвращаемся на экран,
//        // перезагружаем список задач.
//        viewModel.loadTasks()
//    }

    Scaffold(
        topBar = {
            TopAppBar(

                navigationIcon = {
                    IconButton(onClick = onBackFromTaskList) {
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
                                sortTaskList(coroutineScope, viewModel, lazyListState, selectedSortField)
                            },
                            text = { Text("По дате создания") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                selectedSortField = "updatedAt"
                                showSortMenu = false
                                sortTaskList(coroutineScope, viewModel, lazyListState, selectedSortField)
                            },
                            text = { Text("По дате обновления") }
                        )

                        DropdownMenuItem(
                            onClick = {
                                selectedSortField = "name"
                                showSortMenu = false
                                sortTaskList(coroutineScope, viewModel, lazyListState, selectedSortField)
                            },
                            text = { Text("По теме") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                selectedSortField = "subject"
                                showSortMenu = false
                                sortTaskList(coroutineScope, viewModel, lazyListState, selectedSortField)
                            },
                            text = { Text("По содержимому") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                selectedSortField = "id"
                                showSortMenu = false
                                sortTaskList(coroutineScope, viewModel, lazyListState, selectedSortField)
                            },
                            text = { Text("По номеру") }
                        )

                        val isDescending2 = true
                        val isDescending1 = false


                        DropdownMenuItem(
                            onClick = {
                                selectedSortField = "status"
                                showSortMenu = false
                                sortTaskList(coroutineScope, viewModel, lazyListState, selectedSortField)
                            },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val sortIcon: ImageVector = if (isDescending1) {
                                        Icons.Filled.ArrowDownward
                                    } else {
                                        Icons.Filled.ArrowUpward
                                    }
                                    Icon(sortIcon, contentDescription = "Sort Icon")
                                    Spacer(modifier = Modifier.width(8.dp)) // отступ между иконкой и текстом
                                    Text("По статусу")
                                }
                            }
                        )
                        DropdownMenuItem(
                            onClick = {
                                selectedSortField = "status_dsc"
                                showSortMenu = false
                                sortTaskList(coroutineScope, viewModel, lazyListState, selectedSortField)
                            },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val sortIcon: ImageVector = if (isDescending2) {
                                        Icons.Filled.ArrowDownward
                                    } else {
                                        Icons.Filled.ArrowUpward
                                    }
                                    Icon(sortIcon, contentDescription = "Sort Icon")
                                    Spacer(modifier = Modifier.width(8.dp)) // отступ между иконкой и текстом
                                    Text("По статусу")
                                }
                            }
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


                    if (Const.LOCAL_REPO) {
                        IconButton(onClick = { showDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Очистить базу"
                            )
                        }
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewTask
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
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else
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
                    state = lazyListState,
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
                        TaskItem(
                            task = tasksContent,
                            onRemove = viewModel::removeItem,
                            onClick = onItemClick
                        )
                    }
                }
            }
    }


}


fun sortTaskList(
    coroutineScope: CoroutineScope,
    viewModel: TaskListViewModel,
    lazyListState: LazyListState,
    selectedSortField: String) {

    viewModel.sortTasks(selectedSortField)
    coroutineScope.launch {
        lazyListState.animateScrollToItem(0)
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