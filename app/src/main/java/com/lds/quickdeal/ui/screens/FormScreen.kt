package com.lds.quickdeal.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lds.quickdeal.R
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.config.Const.Companion.DEFAULT_OWNERS
import com.lds.quickdeal.android.config.SettingsPreferencesKeys
import com.lds.quickdeal.android.entity.TaskStatus
import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.android.utils.AttachFileType
import com.lds.quickdeal.android.utils.UriUtils
import com.lds.quickdeal.megaplan.entity.Owner
import com.lds.quickdeal.megaplan.entity.TaskRequest
import com.lds.quickdeal.ui.AddFileOrCaptureButton
import com.lds.quickdeal.ui.LoadingAnimation
import com.lds.quickdeal.ui.LogoutButton
import com.lds.quickdeal.ui.MakeDropdownMenu
import com.lds.quickdeal.ui.MakeLocationMenu
import com.lds.quickdeal.ui.SpeechToTextButton

import com.lds.quickdeal.ui.viewmodels.TaskViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


//var shareVideo: Uri? = null //Share this
//var shareFileUri: Uri? = null
//var selectedFilesUris: List<MPFile<Any>>? = null //Share this
//private var videoUri: Uri? = null


var tmpVal: Uri? = Uri.EMPTY
lateinit var fusedLocationClient: FusedLocationProviderClient


@ExperimentalMaterial3Api
@Composable
fun FormScreen(
    navController: NavController,
    taskId: String = "",
    viewModel: TaskViewModel = hiltViewModel()
) {

    val currentTask by viewModel.currentTask.observeAsState(
        UploaderTask(
            -1, "", "", false,
            TaskStatus.NONE, "", "", ""
        )
    )
    LaunchedEffect(taskId) {
        viewModel.setTaskForEditing(taskId)
    }


    var context = LocalContext.current
    val geocoder = Geocoder(context, Locale.getDefault())

    val isTaskRunning by viewModel.isTaskRunning.observeAsState(false)

    //var description by remember { mutableStateOf(if (BuildConfig.DEBUG) "Test Description" else "") }
    //var description by rememberSaveable { mutableStateOf(if (BuildConfig.DEBUG) "Test Description" else "") }
    //val description by viewModel.description.observeAsState("")
    //var topic by rememberSaveable { mutableStateOf("Задача $currentDateTime") }

    // Состояние для хранения координат
    var latitude by rememberSaveable { mutableStateOf<Double?>(null) }
    var longitude by rememberSaveable { mutableStateOf<Double?>(null) }
    var address by rememberSaveable { mutableStateOf<String>("Адрес не определён") }


    LaunchedEffect(latitude, longitude) {
        if (latitude != null && longitude != null) {
            var tmp = geocoder.getAddress(latitude!!, longitude!!)
            if (tmp != null) {
                println("Адрес: ${tmp.getAddressLine(0)}")
                address = tmp.getAddressLine(0)
            } else {
                println("Не удалось получить адрес")
            }
        }
    }

    val locationCallback: (Location?) -> Unit = { location: Location? ->
        if (location != null) {
            latitude = location.latitude
            longitude = location.longitude
//            if (!addresses.isNullOrEmpty()) {
//                address = addresses[0].getAddressLine(0) // Получаем полный адрес
//            } else {
//                address = "Адрес не найден"
//            }
            Toast.makeText(context, "Геометка добавлена", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                context,
                "Не удалось получить местоположение",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    //geolocation
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener(locationCallback)
        } else {
            Toast.makeText(
                context,
                "Разрешение на доступ к местоположению не предоставлено",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    var isLoading by rememberSaveable { mutableStateOf(false) }

    var shareVideo by rememberSaveable { mutableStateOf<Uri?>(null) }
    //var shareFileUri by remember { mutableStateOf<Uri?>(null) }

    //var videoUri by remember { mutableStateOf<Uri?>(null) }
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var selectedFilesUris by rememberSaveable { mutableStateOf<List<MPFile<Any>>?>(null) }

    var photoFile: File? = null
    //var photoUri: Uri? = null //Share this


//    var selectedResponsible by remember { mutableStateOf(
//        if (DEFAULT_OWNERS.isEmpty()) null else DEFAULT_OWNERS[0]
//    ) }

    var selectedResponsible by rememberSaveable {
        mutableStateOf(DEFAULT_OWNERS.getOrNull(0))
    }

    var expanded by rememberSaveable { mutableStateOf(false) }

    val icon = painterResource(id = R.drawable.ic_settings)


    //Camera
//    val photoLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()
//    ) { bitmap: Bitmap? ->
//        if (bitmap != null) {
//            Toast.makeText(context, "Фото сделано!", Toast.LENGTH_SHORT).show()
//                ...
//        } else {
//            Toast.makeText(context, "Фото не сделано", Toast.LENGTH_SHORT).show()
//        }
//    }


    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        photoUri = tmpVal
        if (isSuccess && photoUri != null) {
            Toast.makeText(context, "Фото сделано! $photoUri", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Фото не сделано", Toast.LENGTH_LONG).show()
        }
    }

    val videoCaptureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val videoUri: Uri? = result.data?.data
            videoUri?.let {
                // Здесь обрабатываем URI видео, например, показываем Toast
                Toast.makeText(context, "Видео записано: $it", Toast.LENGTH_LONG).show()
            }
            shareVideo = videoUri
        } else {
            Toast.makeText(context, "Запись видео отменена", Toast.LENGTH_LONG).show()
        }
    }

    // Лаунчер для разрешения на использование микрофона (для видео)
    val microphonePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchVideoRecording(context, videoCaptureLauncher)
        } else {
            Toast.makeText(
                context,
                "Разрешение на использование микрофона не предоставлено",
                Toast.LENGTH_SHORT
            ).show()


//            if (checkPermissionStatus(context, Manifest.permission.RECORD_AUDIO)) {
//                Toast.makeText(navController.context, "@@@", Toast.LENGTH_SHORT).show()
//            }

            launchVideoRecording(context, videoCaptureLauncher)
        }
    }


//    val videoCaptureLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == android.app.Activity.RESULT_OK) {
//            val videoUri: Uri? = result.data?.data
//            // Обработка URI видео
//            videoUri?.let {
//                // например, покажем Toast с URI
//                Toast.makeText(navController.context, "Видео записано: $it", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(navController.context, "Запись видео отменена", Toast.LENGTH_SHORT).show()
//        }
//    }

    val cameraPermissionLauncherForVideo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            // Разрешение не получено, покажем сообщение
            Toast.makeText(
                context, "Разрешение на использование камеры не предоставлено",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val cameraPermissionLauncherForPhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            photoFile = try {
                File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
            } catch (e: IOException) {
                null
            }
            tmpVal = createImageUri(context)
            if (tmpVal != null) {
                photoLauncher.launch(tmpVal!!)
            } else {
                Toast.makeText(context, "Не удалось создать файл для фото", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            // Разрешение не получено, покажем сообщение
            Toast.makeText(
                context,
                "Разрешение на использование камеры не предоставлено",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    var showErrorDialog by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (showErrorDialog) {
        AlertDialog(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),


            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(text = "Ошибка")
            },
            text = {
                Text(text = errorMessage)
            },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("ОК")
                }
            }
        )
    }



    Box(modifier = Modifier.fillMaxSize()) {
        // Основной контент
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                // Добавление тулбара с кнопкой перехода на экран настроек


                TopAppBar(
                    title = {
                        Text(
                            text = if (taskId.isEmpty()) "Создать заявку" else "Заявка №${currentTask?.megaplanId}"
                        )
                    },
                    actions = {
//                IconButton(onClick = {
//                    navController.navigate("settings") // Переход на экран настроек
//                }) {
//                    Icon(painter = icon, contentDescription = "Настройки")
//                }
                        if (taskId.isEmpty()) {
                            IconButton(onClick = {
                                navController.navigate("tasks") // Переход на экран созданных задач
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.FormatListNumbered, // Иконка в виде списка
                                    contentDescription = "Созданные задачи"
                                )
                            }
                        }



                        IconButton(onClick = {
                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    fusedLocationClient.lastLocation.addOnSuccessListener(
                                        locationCallback
                                    )
                                }

                                else -> {
                                    // Запрашиваем разрешение на доступ к местоположению
                                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.MyLocation,
                                contentDescription = "Добавить геометку"
                            )
                        }

                        if (taskId.isEmpty()) {
                            LogoutButton(navController, context)
                        }

                    }
                )


                //MAIN CONTAINER
                OutlinedTextField(
                    value = currentTask!!.name,
                    onValueChange = { newName -> viewModel.updateTaskName(newName) },
                    label = { Text("Тема") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

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
                    modifier = Modifier
                        .fillMaxWidth()
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


                //=========================
                selectedResponsible?.let {
                    Text("Ответственный", style = MaterialTheme.typography.labelLarge)
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = it.description,
                            onValueChange = {},
                            label = { Text("Ответственный") },
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DEFAULT_OWNERS.forEach { owner ->
                                DropdownMenuItem(
                                    text = { Text("${owner.description} (${owner.id})") },
                                    onClick = {
                                        selectedResponsible = owner
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                //=========================


                if (latitude != null && longitude != null) {

                    MakeLocationMenu(
                        "Широта: %.5f".format(latitude) + " " + "Долгота: %.5f".format(longitude),
                        ""
                    ) {
                        latitude = null
                        longitude = null
                    }

//                Text(
//                    text = "Широта: %.5f".format(latitude) + " " + "Долгота: %.5f".format(longitude), // Округляем до 5 знаков после запятой
//                    style = MaterialTheme.typography.titleSmall
//                )


//                Text(
//                    text = "Долгота: %.5f".format(longitude),
//                    style = MaterialTheme.typography.titleSmall
//                )
                }
//        Column(modifier = Modifier.padding(16.dp)) {
//            if (latitude != null && longitude != null) {
////                Text(
////                    text = "Широта: %.5f".format(latitude) + " " + "Долгота: %.5f".format(longitude), // Округляем до 5 знаков после запятой
////                    style = MaterialTheme.typography.titleSmall
////                )
//
//
////                Text(
////                    text = "Долгота: %.5f".format(longitude),
////                    style = MaterialTheme.typography.titleSmall
////                )
//            } else {
////                Text(
////                    text = "Географические координаты не получены.",
////                    style = MaterialTheme.typography.bodySmall,
////                    color = Color.Gray
////                )
//            }
//        }
                //Attach information
                Column(modifier = Modifier.padding(16.dp)) {
                    // Пример: Если shareVideo не пустое, отображаем поле
                    shareVideo?.let {
                        UriUtils.getFileName(context, it)?.let { it1 ->
//                    TextField(
//                        value = it1,
//                        onValueChange = {},
//                        label = { Text("Share Video URI") },
//                        readOnly = true,
//                        modifier = Modifier.fillMaxWidth()
//                    )
                            val totalSize = UriUtils.formatFileSize(context, it)
                            val fileName = UriUtils.getFileName(context, it).toString()
                            MakeDropdownMenu(fileName, totalSize.toString(), AttachFileType.VIDEO) {
                                shareVideo = null
                            }
                        }
                    }

                    // Пример: Если shareFileUri не пустое, отображаем поле
//            shareFileUri?.let {
//                TextField(
//                    value = it.toString(),
//                    onValueChange = {},
//                    label = { Text("Share File URI") },
//                    readOnly = true,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }

                    // Пример: Если selectedFilesUris не пустое, отображаем информацию о файлах
                    selectedFilesUris?.takeIf { it.isNotEmpty() }?.let { files ->
                        // Проходим по каждому элементу списка selectedFilesUris
//                files.forEach { file ->
//                    val rawUri = file.platformFile.toString()
//                    val uri = Uri.parse(rawUri)
//
//                    val fileName = UriUtils.getFileName(context, uri)
//                    println("Имя файла: $fileName")
//                }

                        // Отображаем TextField с именами файлов
//                TextField(
//                    value = files.joinToString(", ") { file ->
//                        val rawUri = file.platformFile.toString()
//                        val uri = Uri.parse(rawUri)
//                        UriUtils.getFileName(context, uri).toString()
//                    },
//                    onValueChange = {},
//                    label = { Text("Selected Files") },
//                    readOnly = true,
//                    modifier = Modifier.fillMaxWidth()
//                )

                        files.forEachIndexed { index, file ->
                            val rawUri =
                                file.platformFile.toString() // Получаем строковое представление URI
                            val uri = Uri.parse(rawUri)
                            val fileName =
                                UriUtils.getFileName(context, uri).toString() // Извлекаем имя файла

//                    TextField(
//                        value = fileName, // Отображаем имя файла
//                        onValueChange = {}, // Поле read-only, поэтому обработчик не нужен
//                        label = { Text("Selected File") },
//                        readOnly = true,
//                        modifier = Modifier.fillMaxWidth()
//                    )


                            val totalSize = UriUtils.formatFileSize(context, uri)

                            MakeDropdownMenu(fileName, totalSize.toString(), AttachFileType.FILE) {
                                selectedFilesUris = selectedFilesUris?.let { currentList ->
                                    if (index in currentList.indices) {
                                        currentList.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    } else {
                                        currentList // Если индекс не найден, оставляем список без изменений
                                    }
                                }
                            }
                        }
                    }


                    // Пример: Если photoUri не пустое, отображаем поле
                    photoUri?.let {


//                TextField(
//                    value = it.toString(),
//                    onValueChange = {},
//                    label = { Text("Фото") },
//                    readOnly = true,
//                    modifier = Modifier.fillMaxWidth()
//                )
                        var title = UriUtils.getFileName(context, it)
                        val totalSize = UriUtils.formatFileSize(context, it)

                        if (title != null) {
                            MakeDropdownMenu(title, totalSize.toString(), AttachFileType.PHOTO) {
                                photoUri = null
                            }
                        }
                    }
                }


//        Button(onClick = { /* Надиктовать текст */ }, modifier = Modifier.padding(top = 8.dp)) {
//            Text("Надиктовать содержание")
//        }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SpeechToTextButton(context) {
                        viewModel.appendDescription("\n $it")
                    }
                }

                //AddFileButton()
                AddFileOrCaptureButton(
                    { newFiles ->
                        selectedFilesUris = newFiles // Обновляем selectedFilesUris через колбэк
                    },
                    cameraPermissionLauncherForPhoto,
                    cameraPermissionLauncherForVideo,
                    microphonePermissionLauncher
                )




                Button(
                    onClick = {

                        if (isTaskRunning) {
                            viewModel.cancelTask()
                            isLoading = false
                            Toast.makeText(context, "Задача отменена", Toast.LENGTH_SHORT).show()
                        } else {

                            isLoading = true

                            val vacancyId = "1018054"
                            val taskRequest = TaskRequest(
                                name = currentTask.name,
                                subject = currentTask.subject
                                //    attaches = attaches,
                                //    auditors = auditors,
                                //    executors = executors,
                                // Временно убрали parent = Parent(contentType = "Task", id = vacancyId) // ID родительской задачи
                                ,
                                isTemplate = false //Добавили
                                ,
                                isUrgent = false //Добавили
                                ,
                                latitude = latitude,
                                longitude = longitude,
                                megaplanId = currentTask.megaplanId
                            )
                            // ID ответственного | owner = Owner(contentType = "Employee", id = "1000093"),
                            selectedResponsible?.let {
                                taskRequest.responsible = Owner(
                                    contentType = "Employee",//it.contentType
                                    id = it.id
                                )
                            }

                            viewModel.createTask(
                                taskRequest,
                                selectedFilesUris,
                                photoUri,
                                shareVideo,
                                { taskResponse ->
                                    isLoading = false
                                    Toast.makeText(context, "Задача добавлена!", Toast.LENGTH_SHORT)
                                        .show()
                                },
                                { err ->
                                    isLoading = false
                                    Toast.makeText(context, "Ошибка: $err", Toast.LENGTH_LONG)
                                        .show()
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTaskRunning) Color.Red else MaterialTheme.colorScheme.primary

                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.End)
                ) {
                    if (isTaskRunning) {
                        Icon(Icons.Filled.Close, contentDescription = "Отменить")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Отменить ")
                    } else {
                        Icon(Icons.Filled.Send, contentDescription = "Отправить")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Отправить")
                    }
                }
                //END MAIN CONTAINER

            }
        }

        // Прогресс-бар, накладываемый поверх контента
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x2C000000)) // Полупрозрачный фон
                    .wrapContentSize(Alignment.Center)
            ) {
                LoadingAnimation()
                //CircularProgressIndicator()
            }
        }
    }


}

fun getCurrentTime(): String {
    val currentTime = Calendar.getInstance().time
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(currentTime)
}


@Preview(showBackground = true)
@Composable
fun PreviewLoadingAnimation() {
    LoadingAnimation()
}


fun logout(context: Context) {
    val prefs = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE)
    prefs.edit().apply {
        putString(SettingsPreferencesKeys.AD_USERNAME, "")
        putString(SettingsPreferencesKeys.AD_PASSWORD, "")
        apply()
    }
}

private fun checkPermissionStatus(context: Context, permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
}

fun createVideoUri(context: Context): Uri? {
    return try {
        val videoName = "video_${System.currentTimeMillis()}.mp4"
        val contentValues = android.content.ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, videoName)
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies")
        }

        context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


fun createImageUri(context: Context): Uri? {
//    photoFile?.let {
//        FileProvider.getUriForFile(
//            context,
//            "${context.packageName}.provider",
//            it
//        )
//    }
    return try {
        // Создаем имя файла с текущим временем
        val imageName = "photo_${System.currentTimeMillis()}.jpg"
        val contentValues = android.content.ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures"
            )  // Папка "photo" в "Pictures"
        }

        // Получаем URI для сохранения изображения в MediaStore
        val contentResolver = context.contentResolver
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun launchVideoRecording(
    context: Context,
    videoCaptureLauncher: ActivityResultLauncher<Intent> // Используем правильный тип
) {
    val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
    val videoUri = createVideoUri(context)
    if (videoUri != null) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        videoCaptureLauncher.launch(intent) // Запускаем запись
    } else {
        Toast.makeText(context, "Ошибка создания URI для видео", Toast.LENGTH_SHORT).show()
    }
}

private suspend fun Geocoder.getAddress(
    latitude: Double,
    longitude: Double,
): Address? = withContext(Dispatchers.IO) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCoroutine { cont ->
                getFromLocation(latitude, longitude, 1) {
                    cont.resume(it.firstOrNull())
                }
            }
        } else {
            suspendCoroutine { cont ->
                @Suppress("DEPRECATION")
                val address = getFromLocation(latitude, longitude, 1)?.firstOrNull()
                cont.resume(address)
            }
        }
    } catch (e: Exception) {
        println(e)
        null
    }
}

//@Composable
//fun AddFileButton() {
//    val context = LocalContext.current
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        // Обработка выбранного файла
//        if (uri != null) {
//            Toast.makeText(context, "Выбран файл: $uri", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(context, "Файл не выбран", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    Button(
//        onClick = {
//            // Запустить выбор файла
//            launcher.launch("*/*") // Можно указать "image/*", "video/*", или другие MIME-типы
//        }
//    ) {
//        Text("Добавить фото/видео/файлы")
//    }
//}