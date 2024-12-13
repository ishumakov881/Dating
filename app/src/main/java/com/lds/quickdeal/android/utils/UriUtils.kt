package com.lds.quickdeal.android.utils

import android.content.Context
import android.net.Uri
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
    }
}