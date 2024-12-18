package com.lds.quickdeal.android.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns

class UriUtils {


    companion object {
        fun getFileName(context: Context, uri: Uri): String? {
            val contentResolver = context.contentResolver
            val projection = arrayOf(OpenableColumns.DISPLAY_NAME) // Запрашиваем имя файла

            contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val name =
                        cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                    return name
                }
            }
            return null
        }

        fun getFileExtension(name: String): String {
            val extension = name.substringAfterLast('.', "")
            return extension
        }


        @SuppressLint("DefaultLocale")
        fun formatFileSize(context: Context, it: Uri): String {

            val sizeInBytes = getFileSize(context, it)

            val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")
            var size = sizeInBytes.toDouble()
            var unitIndex = 0

            while (size >= 1024 && unitIndex < units.size - 1) {
                size /= 1024
                unitIndex++
            }

            return String.format("%.2f %s", size, units[unitIndex])
        }

        fun getFileSize(context: Context, it: Uri): Long {
            val parcelFileDescriptor: ParcelFileDescriptor
            var sizeInBytes = 0L
            try {
                parcelFileDescriptor = context.contentResolver.openFileDescriptor(it, "r")!!
                sizeInBytes = parcelFileDescriptor.statSize ?: 0
                parcelFileDescriptor.close()
            } catch (_: Exception) {
            }
            return sizeInBytes
        }
    }
}