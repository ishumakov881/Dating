package com.lds.quickdeal.ui.form

import android.Manifest
import android.app.Activity
import android.content.ContentValues
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.frazo.audio_services.player.AudioPlayingData
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lds.quickdeal.R
import com.lds.quickdeal.android.config.Const

import com.lds.quickdeal.android.config.SettingsPreferencesKeys
import com.lds.quickdeal.android.entity.TaskStatus
import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.android.utils.AttachFileType
import com.lds.quickdeal.android.utils.UriUtils
import com.lds.quickdeal.megaplan.entity.Responsible
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


@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalMaterial3Api
@Composable
fun FormScreen(
    navController: NavController,
    _taskId: String = "",
    viewModel: TaskViewModel = hiltViewModel()
) {

    val currentTask by viewModel.currentTask.observeAsState(
        UploaderTask(
            -1, "", "", false,
            TaskStatus.NONE, "", "", "", ""
        )
    )
    LaunchedEffect(_taskId) {
        viewModel.setTaskForEditing(_taskId)
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

    val audioNoteStatus by viewModel.audioStatus.collectAsState()
    val audioPlayingData by viewModel.audioNotePlayingData.collectAsState()
    val audioData by viewModel.audioRecordFlow.collectAsState()

    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)


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

//    var selectedResponsible by rememberSaveable {
//        mutableStateOf(DEFAULT_OWNERS.getOrNull(0))
//    }

    val owners by viewModel.owners.collectAsState()
    val selectedResponsible by viewModel.selectedResponsible.collectAsState()

    var expanded by rememberSaveable { mutableStateOf(false) }

    val icon = painterResource(id = R.drawable.ic_settings)



    val servers by viewModel.servers.collectAsState()
    val selectedServer by viewModel.selectedServer.collectAsState()

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
        if (result.resultCode == Activity.RESULT_OK) {
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
                TopAppBar(

                    navigationIcon = if (!currentTask.isNewTask()) {
                        {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Закрыть"
                                )
                            }
                        }
                    } else ({
                    }),

                    title = {
                        Text(
                            text = if (currentTask.isNewTask()) "Создать заявку" else "Заявка №${currentTask?.megaplanId}"
                        )
                    },
                    actions = {
//                IconButton(onClick = {
//                    navController.navigate("settings") // Переход на экран настроек
//                }) {
//                    Icon(painter = icon, contentDescription = "Настройки")
//                }


                        if (currentTask.isNewTask()) {
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

                        if (currentTask.isNewTask()) {
                            LogoutButton(navController, context)
                        }

                    }
                )


                //MAIN CONTAINER
                TitileWithPresetes(currentTask, viewModel)
                DescriptionWithPresets(currentTask, viewModel)


                //=========================
                if (owners.isEmpty()) {
                    OutlinedTextField(
                        value = "Список пуст",
                        onValueChange = {},
                        label = { Text("Ответственный") },//
                        readOnly = true,
                        enabled = false, // Поле недоступно для взаимодействия
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                } else {
                    selectedResponsible?.let {
                        //Text("Ответственный", style = MaterialTheme.typography.labelLarge)
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
                                    .padding(top = 16.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                owners.forEach { owner ->

                                    //showAvatar(owner.avatar)

                                    DropdownMenuItem(
                                        text = {
                                            //Text("${owner.description} (${owner.id})")
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Start,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {

                                                //println("AVATAR: ${owner.avatar}")

                                                AsyncImage(
                                                    model = ImageRequest.Builder(LocalContext.current)
                                                        .data(owner.avatar)
                                                        .crossfade(true)
                                                        .build(),
                                                    placeholder = painterResource(R.drawable.ic_person_placeholder),
                                                    error = painterResource(R.drawable.ic_person_placeholder),
                                                    //error = painterResource(R.drawable.ic_person_placeholder),

                                                    contentDescription = "Аватар ${owner.description}",
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop,
                                                    onError = { error ->
                                                        println("AsyncImage Ошибка загрузки аватара для ${owner.description}: ${error.result.throwable.message}")
                                                    }
                                                )
                                                //Text("${owner.avatar}")

                                                Spacer(modifier = Modifier.width(12.dp))

                                                var tmp =
                                                    if (owner.position.isEmpty()) owner.megaplanUserId else owner.position

                                                val title = buildAnnotatedString {
                                                    append(owner.description)
                                                    if (tmp.isNotEmpty()) {
                                                        append(" ")
                                                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                                            append("(")
                                                            append(tmp)
                                                            append(")")
                                                        }
                                                    }
                                                }
                                                Text(
                                                    text = title,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        },
                                        onClick = {
                                            viewModel.updateResponsible(owner)
                                            expanded = false
                                        }
                                    )
                                    //val sizeResolver = rememberConstraintsSizeResolver()


                                }
                            }
                        }
                    }


                    ServerDropdown(
                        servers = servers,
                        selectedServer = selectedServer,
                        onServerSelected = { server ->
                            viewModel.setServer(server)
                        }
                    )
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

                println("audio state: $audioNoteStatus")
                //if (audioNoteStatus == TaskViewModel.AudioNoteStatus.HAVE_TO_RECORD) {


//                when (permissionState.status) {
//                    PermissionStatus.Granted -> {
//                        Text("Разрешение предоставлено!")
//                    }
//
//                    is PermissionStatus.Denied -> {
//                        if ((permissionState.status as PermissionStatus.Denied).shouldShowRationale) {
//                            Text("Для записи звука требуется ваше разрешение.")
//                        } else {
//                            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                                data = Uri.fromParts("package", context.packageName, null)
//                            }
//                            context.startActivity(intent)
//                        }
//                        //Запросить разрешение
//                        IconButton(
//                            onClick = { permissionState.launchPermissionRequest() },
//                            ) {
//                            Icon(
//                                imageVector = Icons.Filled.Mic,
//                                contentDescription = "Start Recording",
//                                //modifier = Modifier.size(48.dp)
//                            )
//                        }
//                    }
//                }

//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(50.dp),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//
//
//                    AudioRecorder(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(horizontal = 12.dp),
//                        recordIcon = {
//                            //Icons.Filled.PlayCircleFilled
//                            Icon(
//                                imageVector = Icons.Filled.Mic,
//                                contentDescription = "Start Recording",
//                                //modifier = Modifier.size(48.dp)
//                            )
//                        },
//                        stopIcon = {
//                            //Icons.Filled.StopCircle
//                            Icon(
//                                imageVector = Icons.Filled.Stop,
//                                contentDescription = "Stop Recording",
//                                //modifier = Modifier.size(48.dp)
//                            )
//                        },
//
//                        onRecordRequested = { viewModel.startRecordingAudioNote() },
//                        onStopRequested = { viewModel.stopRecordingAudio() },
//                        audioRecordingData = audioData,
//                            recordingWaveVisualizer = MirrorWaveRecordingVisualizer(
//                                wavePaint = Paint().apply {
////                                    color = Color(0xFFFF1744).toArgb()//LocalContentColor.current.toArgb()
////                                    strokeWidth = 2f
////                                    style = Paint.Style.STROKE
////                                    strokeCap = Paint.Cap.ROUND
////                                    flags = Paint.ANTI_ALIAS_FLAG
////                                    strokeJoin = Paint.Join.BEVEL
//
//                                    color = android.graphics.Color.BLACK
//                                    strokeWidth = 2f
//                                    style = Paint.Style.STROKE
//                                    strokeCap = Paint.Cap.ROUND
//                                    flags = Paint.ANTI_ALIAS_FLAG
//                                    strokeJoin = Paint.Join.BEVEL
//                                },
//                                middleLinePaint = Paint().apply {
//                                    color = LocalTextSelectionColors.current.handleColor.toArgb()
//                                    style = Paint.Style.FILL_AND_STROKE
//                                    strokeWidth = 2f
//                                    pathEffect = DashPathEffect(arrayOf(4f, 4f).toFloatArray(), 0f)
//                                }
//                            )
//                    )
//                    //}
//
//
////                    var sliderValue = 77.0f
////                    Slider(
////                        value = sliderValue,
////                        onValueChange = { sliderValue = it },
////                        modifier = Modifier.padding(16.dp),
////                        valueRange = 0f..100f,
////                        steps = 10
////                    )
//
//                    MyAudioPlayer(audioPlayingData)
//                }

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

                            //val vacancyId = "1018054"
                            val taskRequest = TaskRequest(
                                name = currentTask.name,
                                subject = currentTask.subject
                                //    attaches = attaches,
                                //    auditors = auditors,
                                //    executors = executors,
                                // Временно убрали parent = Parent(contentType = "Task", id = vacancyId) // ID родительской задачи
                                , isTemplate = false //Добавили
                                , isUrgent = false //Добавили
                                , latitude = latitude,
                                longitude = longitude,
                                megaplanId = currentTask.megaplanId
                            )
                            // ID ответственного | owner = Owner(contentType = "Employee", id = "1000093"),
                            selectedResponsible?.let {
                                taskRequest.responsible = Responsible(
                                    contentType = "Employee",//it.contentType
                                    id = it.megaplanUserId,
                                )
                            }


                            //println("[*]$taskRequest")

                            viewModel.createTask(
                                taskRequest,
                                selectedFilesUris,
                                photoUri,
                                shareVideo,

                                { taskResponse ->
                                    isLoading = false
                                    if (currentTask.megaplanId.isEmpty()) {
                                        Toast.makeText(
                                            context,
                                            "Задача добавлена!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Задача обновлена!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    navController.navigate("tasks")
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
                    .background(Color(0x2C000000))
                    .wrapContentSize(Alignment.Center)
            ) {
                LoadingAnimation()
                //CircularProgressIndicator()
            }
        }
    }


}


//@Composable
//fun showAvatar(avatar: String) {
//
//    AsyncImage(
//        model = avatar,
//        contentDescription = null,
//    )
////    AsyncImage(
////        model = avatar,
////        contentDescription = "x",
////        modifier = Modifier
////            .size(24.dp)
////            .clip(CircleShape),
////        contentScale = ContentScale.Crop
////    )
////
////
////    val painter = rememberAsyncImagePainter("https://ktor.io/static/bg-primary-desktop-dd085c53689aae94cac3264a5e7e7657.png")
////
////    val state by painter.state.collectAsState()
////    if (state is AsyncImagePainter.State.Success && (state as AsyncImagePainter.State.Success).result.dataSource != DataSource.MEMORY_CACHE) {
////        // Perform the transition animation.
////    }
////
////    Image(
////        painter = painter,
////        contentDescription = null,
////    )
//
////                                                SubcomposeAsyncImage(
////                                                    model = owner.avatar,
////                                                    loading = {
////                                                        CircularProgressIndicator()
////                                                    },
////                                                    contentDescription = null,
////                                                )
////                                                AsyncImage(
//////                                                    model = owner.avatar,
//////                                                    contentDescription = "Аватар ${owner.description}"
////////                                                    modifier = Modifier.size(100.dp)
////////                                                    .clip(CircleShape),
////////                                                    contentScale = ContentScale.Crop
////
////                                                    model = ImageRequest.Builder(LocalContext.current)
////                                                        .data(owner.avatar)
////                                                        //.crossfade(true)
////                                                        .build(),
//////                                                    //placeholder = painterResource(R.drawable.placeholder),
////                                                    contentDescription = "Аватар ${owner.description}",
//////                                                    contentScale = ContentScale.Crop,
////                                                    modifier = Modifier.clip(CircleShape),
////                                                )
//}

@Composable
fun MyAudioPlayer(audioPlayingData: AudioPlayingData) {
//    AudioPlayer(
//        modifier = Modifier.padding(horizontal = 12.dp),
//        audioPlayingData = audioPlayingData,
//        audioPlayerParams = rememberAudioPlayerParams(
//            playIcon = {
//                Icon(
//                    imageVector = Icons.Default.PlayArrow,
//                    contentDescription = "Play",
//                    modifier = Modifier.size(48.dp)
//                )
//            },
//            pauseIcon = {
//                Icon(
//                    imageVector = Icons.Default.Pause,
//                    contentDescription = "Pause",
//                    modifier = Modifier.size(48.dp)
//                )
//            },
//            endIcon =
//            //if (onAudioNoteDeleteRequest != null) {
//            {
//                Icon(
//                    imageVector = Icons.Default.Delete,
//                    contentDescription = "Delete",
//                    modifier = Modifier.size(48.dp)
//                )
//            }
//            //} else null,
//        ),
//
//        audioPlayerCallbacks = AudioPlayerCallbacks(
//            onPlay = {},//onAudioNotePlayRequest,
//            onPause = {},//onAudioNotePauseRequest,
//            onEndIconClicked = {},//onAudioNoteDeleteRequest,
//            onSeekPosition = {}//onAudionNoteSeekPosition
//        )
//    )
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
        val contentValues = ContentValues().apply {
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
        val contentValues = ContentValues().apply {
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