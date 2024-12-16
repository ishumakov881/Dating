package com.lds.quickdeal.ui.screens

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.darkrockstudios.libraries.mpfilepicker.MultipleFilePicker
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.R
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.config.OwnerWrapper
import com.lds.quickdeal.android.config.SettingsPreferencesKeys
import com.lds.quickdeal.android.utils.AttachFileType
import com.lds.quickdeal.android.utils.PermissionResolver
import com.lds.quickdeal.android.utils.UriUtils
import com.lds.quickdeal.megaplan.entity.Owner
import com.lds.quickdeal.ui.LoadingAnimation

import com.lds.quickdeal.ui.viewmodels.TaskViewModel
import kotlinx.io.IOException
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//var shareVideo: Uri? = null //Share this
//var shareFileUri: Uri? = null
//var selectedFilesUris: List<MPFile<Any>>? = null //Share this
//private var videoUri: Uri? = null


var tmpVal: Uri? = Uri.EMPTY

@ExperimentalMaterial3Api
@Composable
fun FormScreen(navController: NavController, taskViewModel: TaskViewModel = hiltViewModel()) {


    var isLoading by remember { mutableStateOf(false) }

    var shareVideo by remember { mutableStateOf<Uri?>(null) }
    //var shareFileUri by remember { mutableStateOf<Uri?>(null) }

    //var videoUri by remember { mutableStateOf<Uri?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFilesUris by remember { mutableStateOf<List<MPFile<Any>>?>(null) }

    var photoFile: File? = null
    //var photoUri: Uri? = null //Share this


    var selectedResponsible by remember { mutableStateOf(Const.DEFAULT_RESPONSIBLE) }

    var expanded by remember { mutableStateOf(false) }

    val currentDateTime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        .format(Date())

    var topic by remember { mutableStateOf("Задача $currentDateTime") }

    var description by remember { mutableStateOf(if (BuildConfig.DEBUG) "Test Description" else "") }
    var context = LocalContext.current


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
            Toast.makeText(context, "Фото не сделано", Toast.LENGTH_SHORT).show()
        }
    }

    val videoCaptureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val videoUri: Uri? = result.data?.data
            videoUri?.let {
                // Здесь обрабатываем URI видео, например, показываем Toast
                Toast.makeText(context, "Видео записано: $it", Toast.LENGTH_SHORT).show()
            }
            shareVideo = videoUri
        } else {
            Toast.makeText(context, "Запись видео отменена", Toast.LENGTH_SHORT).show()
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
                context,
                "Разрешение на использование камеры не предоставлено",
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


    val owners = listOf(
        OwnerWrapper(
            contentType = "Employee",
            id = "1000161",
            description = "Гурьева Юлия Валерьевна"
        ), // Default Owner
        OwnerWrapper("Employee", "1000216", "Резниченко Иван Павлович"),
//        Owner("Employee", "1000163")
    )

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (showErrorDialog) {
        AlertDialog(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Добавляем вертикальный скролл


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



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        // Добавление тулбара с кнопкой перехода на экран настроек


        TopAppBar(
            title = { Text(text = "Создать заявку") },
            actions = {
//                IconButton(onClick = {
//                    navController.navigate("settings") // Переход на экран настроек
//                }) {
//                    Icon(painter = icon, contentDescription = "Настройки")
//                }

                IconButton(onClick = {
                    logout(context)
                    navController.navigate("login") {
                        popUpTo("form") { inclusive = true }
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Logout, contentDescription = "Выход")
                }


            }
        )

        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text("Тема") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Содержание") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )


        Text("Ответственный", style = MaterialTheme.typography.labelLarge)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedResponsible.description,
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
                owners.forEach { owner ->
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


                    val fileName = UriUtils.getFileName(context, it).toString()
                    MakeDropdownMenu(fileName, AttachFileType.VIDEO) {
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


                    MakeDropdownMenu(fileName, AttachFileType.FILE) {
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
                var title = UriUtils.getFileName(context, photoUri!!)
                if (title != null) {
                    MakeDropdownMenu(title, AttachFileType.PHOTO) {
                        photoUri = null
                    }
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            // Показываем анимацию загрузки, если `isLoading` true
            if (isLoading) {
                LoadingAnimation()
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


//        Button(onClick = { /* Надиктовать текст */ }, modifier = Modifier.padding(top = 8.dp)) {
//            Text("Надиктовать содержание")
//        }

        SpeechToTextButton(context, {
            description += it
        })

        Button(
            onClick = {

                isLoading = true

                taskViewModel.createTask(topic,
                    description,
                    selectedFilesUris,
                    photoUri,
                    shareVideo,
                    Owner(
                        contentType = selectedResponsible.contentType,
                        id = selectedResponsible.id
                    ),
                    { taskResponse ->
                        isLoading = false
                        if (taskResponse.meta.status == 200) {
                            Toast.makeText(context, "ЗАДАЧА ДОБАВЛЕНА!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Success... {$taskResponse.accessToken}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        //navController.navigate("form")

                    },
                    { err ->
//                    if (BuildConfig.DEBUG) {
//                        errorMessage = "Ошибка при создании задачи: $err"
//                        showErrorDialog = true
//                    } else {
//                        Toast.makeText(
//                            context,
//                            "Ошибка при создании задачи: $err",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
                        isLoading = false
                        println("$err")
                        Toast.makeText(
                            context,
                            "Ошибка при создании задачи: $err",
                            Toast.LENGTH_LONG
                        ).show()
                    })
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.End)
        ) {
            Text("Отправить")
        }
    }


}

@Preview(showBackground = true)
@Composable
fun PreviewLoadingAnimation() {
    LoadingAnimation()
}

@Composable
fun MakeDropdownMenu(title: String, filetype: AttachFileType, onRemoveClicked: () -> Unit) {

    val icon = when (filetype) {
        AttachFileType.FILE -> Icons.Filled.AttachFile
        AttachFileType.PHOTO -> Icons.Filled.Photo
        AttachFileType.VIDEO -> Icons.Filled.VideoFile
        else -> Icons.Filled.Attachment
    }

    var menuExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart), // Размещаем содержимое влево
            verticalAlignment = Alignment.CenterVertically // Центрируем по вертикали
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Attach",
                tint = Color.Red
            )
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { menuExpanded = true }
                    .padding(8.dp)
            )
        }

        // Отображение имени файла

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    menuExpanded = false
                    onRemoveClicked()
                },
                text = { Text("Удалить") },
                modifier = Modifier.padding(horizontal = 16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = "Menu Icon"
                    )
                },
                contentPadding = PaddingValues(horizontal = 12.dp)
            )
        }
    }

}

fun logout(context: Context) {
    val prefs = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE)
    prefs.edit().apply {
        putString(SettingsPreferencesKeys.AD_USERNAME, null)
        putString(SettingsPreferencesKeys.AD_PASSWORD, null)
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


@Composable
fun AddFileOrCaptureButton(

    onFileSelected: (List<MPFile<Any>>?) -> Unit,
    cameraPermissionLauncherForPhoto: ManagedActivityResultLauncher<String, Boolean>,
    cameraPermissionLauncherForVideo: ManagedActivityResultLauncher<String, Boolean>,

    microphonePermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
) {
    val context = LocalContext.current

    var showFilePicker by remember { mutableStateOf(false) }

    val fileType = Const.UPLOAD_FILE_EXT
    if (showFilePicker) {
        MultipleFilePicker(show = showFilePicker, fileExtensions = fileType) { file ->
            showFilePicker = false
            // Обработка выбранного файла
            println("Выбран файл: $file")

            if (file.isNullOrEmpty()) {
                Toast.makeText(context, "Файлы не выбраны", Toast.LENGTH_SHORT).show()
            } else {
                onFileSelected(file)
            }
        }
    }

//    val fileLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        if (uri != null) {
//            Toast.makeText(context, "Выбран файл: $uri", Toast.LENGTH_SHORT).show()
//            shareFileUri = uri
//        } else {
//            Toast.makeText(context, "Файл не выбран", Toast.LENGTH_SHORT).show()
//        }
//    }

    //val icon = painterResource(id = R.drawable.file_upload)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
//        IconButton(onClick = {
//
//            //fileLauncher.launch("*/*")
//            showFilePicker = true
//        }
//
//        ) {
//            Icon(imageVector = Icons.Filled.UploadFile, "Файлы")
//        }


        Button(onClick = {

            //fileLauncher.launch("*/*")
            showFilePicker = true
        }

        ) {

            Text("Файлы")
//            Icon(
//                imageVector = Icons.Filled.UploadFile,
//                contentDescription = "Выбрать файл",
//                tint = Color.White // Цвет иконки
//            )
        }


        Spacer(modifier = Modifier.width(16.dp)) // Отступ между кнопками
        Button(onClick = {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val packageManager = context.packageManager

            if (intent.resolveActivity(packageManager) != null) {
                cameraPermissionLauncherForPhoto.launch(Manifest.permission.CAMERA)
            } else {
                Toast.makeText(
                    context,
                    "На устройстве отсутствует приложение камеры",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }) {
            Text("Фото")//Сделать фото

        }
        Spacer(modifier = Modifier.width(16.dp)) // Отступ между кнопками
        Button(onClick = {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            val packageManager = context.packageManager

            if (intent.resolveActivity(packageManager) != null) {

                if (PermissionResolver.isCameraGranted(context)) {
                    microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                } else {
                    cameraPermissionLauncherForVideo.launch(
                        Manifest.permission.CAMERA
                    )
                }
            } else {
                Toast.makeText(
                    context,
                    "На устройстве отсутствует приложение для записи видео",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text("Видео")
        }
    }
}

@Composable
fun SpeechToTextButton(
    context: Context, onSpeechRecognized: (String) -> Unit
) {
    // Состояние для хранения результата
    val recognizedText = remember { mutableStateOf("") }

    // Лаунчер для получения результата
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                recognizedText.value = matches[0]
                onSpeechRecognized(recognizedText.value)
            }
        }
    }

    Button(
        onClick = {
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        //Locale.getDefault()
                        "ru-RU"
                    )
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите...")
                }
                speechLauncher.launch(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "Ваше устройство не поддерживает распознавание речи",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text("Надиктовать содержание")
    }

    // Отображение надиктованного текста
    if (recognizedText.value.isNotEmpty()) {
        Text(
            text = "Распознанный текст: ${recognizedText.value}",
            modifier = Modifier.padding(top = 16.dp)
        )
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