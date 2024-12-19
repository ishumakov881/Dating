package com.darkrockstudios.libraries.mpfilepicker.newversion

import android.net.Uri
import android.os.Parcelable
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.net.toFile
import com.darkrockstudios.libraries.mpfilepicker.FileSelected
import com.darkrockstudios.libraries.mpfilepicker.FilesSelected
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import kotlinx.parcelize.Parcelize

@Parcelize /*bugfix*/
data class AndroidFile(
    override val path: String,
    override val platformFile: Uri,
) : MPFile<Uri>, Parcelable {
    override suspend fun getFileByteArray(): ByteArray = platformFile.toFile().readBytes()
}

@Composable
public fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    title: String?,
    onFileSelected: FileSelected
) {
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { result ->
        if (result != null) {
            onFileSelected(AndroidFile(result.toString(), result))
        } else {
            onFileSelected(null)
        }
    }

    val mimeTypeMap = MimeTypeMap.getSingleton()
    val mimeTypes = if (fileExtensions.isNotEmpty()) {
        fileExtensions.mapNotNull { ext ->
            mimeTypeMap.getMimeTypeFromExtension(ext)
        }.toTypedArray()
    } else {
        emptyArray()
    }

    LaunchedEffect(show) {
        if (show) {
            launcher.launch(mimeTypes)
        }
    }
}
@Composable
public fun MultipleFilePickerNew(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    title: String?,
    onFileSelected: FilesSelected
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { result ->

        val files = result.mapNotNull { uri ->
            uri.path?.let {path ->
                AndroidFile(path, uri)
            }
        }

        if (files.isNotEmpty()) {
            onFileSelected(files)
        } else {
            onFileSelected(null)
        }
    }

    val mimeTypeMap = MimeTypeMap.getSingleton()
    val mimeTypes = if (fileExtensions.isNotEmpty()) {
        fileExtensions.mapNotNull { ext ->
            mimeTypeMap.getMimeTypeFromExtension(ext)
        }.toTypedArray()
    } else {
        emptyArray()
    }

    LaunchedEffect(show) {
        if (show) {
            launcher.launch(mimeTypes)
        }
    }
}

@Composable
public fun DirectoryPicker(
    show: Boolean,
    initialDirectory: String?,
    title: String?,
    onFileSelected: (String?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) { result ->
        onFileSelected(result?.toString())
    }

    LaunchedEffect(show) {
        if (show) {
            launcher.launch(null)
        }
    }
}