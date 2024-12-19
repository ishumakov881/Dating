package com.lds.quickdeal.ui

import android.Manifest
import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.darkrockstudios.libraries.mpfilepicker.newversion.MultipleFilePickerNew

import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.utils.PermissionResolver

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
        MultipleFilePickerNew(show = showFilePicker, fileExtensions = fileType, initialDirectory = null, title = null) { file ->
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