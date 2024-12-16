package com.lds.quickdeal.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.config.SettingsPreferencesKeys.SettingsPreferencesKeys.PREF_KEY_MEGAPLAN_ACCESS_TOKEN
import com.lds.quickdeal.android.utils.UriUtils
import com.lds.quickdeal.megaplan.entity.Owner
import com.lds.quickdeal.megaplan.entity.TaskRequest
import com.lds.quickdeal.megaplan.entity.TaskResponse


import com.lds.quickdeal.network.TaskErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private var client: HttpClient,
    private var context: Context
) {


//    {
//        "contentType": "Task",
//        "name": "123", "subject": "1234",
//
//        "responsible":{
//        "contentType":"Employee", "id":1000161
//    },
//
//        "isTemplate": false, "isUrgent": false
//    }

    suspend fun createTask(
        topic: String,
        description: String,
        selectedFiles: List<MPFile<Any>>?,
        photoUri: Uri?,
        shareVideo: Uri?,
        responsible: Owner
    ): Result<TaskResponse> {
        val vacancyId = "1018054"
        val taskRequest = TaskRequest(
            name = description,
            subject = topic,
            //    attaches = attaches,
            // Временно убрали owner = Owner(contentType = "Employee", id = "1000093"),
            responsible = Owner(
                contentType = responsible.contentType,
                id = responsible.id
            ), // ID ответственного
            //    auditors = auditors,
            //    executors = executors,
            // Временно убрали parent = Parent(contentType = "Task", id = vacancyId) // ID родительской задачи
            isTemplate = false //Добавили
            , isUrgent = false //Добавили

        )
        return taskCreate(taskRequest, selectedFiles, photoUri, shareVideo)
    }

    suspend fun taskCreate(
        taskRequest: TaskRequest,
        selectedFiles: List<MPFile<Any>>?, photoUri: Uri?, shareVideo: Uri?

    ): Result<TaskResponse> {


        if (taskRequest.subject.isNullOrEmpty()) {
            return Result.failure(Exception("Поле 'Тема' не должно быть пустым"))
        }

        if (taskRequest.name.isNullOrEmpty()) {//Description
            return Result.failure(Exception("Поле 'Содержание' не должно быть пустым"))
        }


//        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//        val tmpName = prefs.getString(PREF_KEY_USERNAME, null)
//
//        if(username.isNotEmpty() && username == tmpName){
//            val storedToken = prefs.getString(PREF_KEY_ACCESS_TOKEN, null)
//            val storedRefreshToken = prefs.getString(PREF_KEY_REFRESH_TOKEN, null)
//            val storedTokenType = prefs.getString(PREF_KEY_TOKEN_TYPE, null)
//            val expiresAt = prefs.getLong(PREF_KEY_EXPIRES_AT, 0L)
//            val currentTime = System.currentTimeMillis() / 1000
//
//            if (!storedToken.isNullOrEmpty() && expiresAt > currentTime) {
//                // Если токен существует и ещё не истёк, возвращаем его.
//                return Result.success(
//                    AuthResponse(
//                        access_token = storedToken,
//                        refresh_token = storedRefreshToken.orEmpty(),
//                        expires_in = (expiresAt - currentTime),
//                        token_type = storedTokenType.orEmpty(),
//                        scope = prefs.getString(PREF_KEY_SCOPE, null)
//                    )
//                )
//            }
//        }

        //,,,,,


        val prefs = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE)
        var accessToken = prefs.getString(PREF_KEY_MEGAPLAN_ACCESS_TOKEN, null)


        //Выбран файл: [AndroidFile(path=/document/msf:1593, platformFile=content://com.android.providers.downloads.documents/document/msf%3A1593), AndroidFile(path=/document/msf:1573, platformFile=content://com.android.providers.downloads.documents/document/msf%3A1573)]


//        val response: HttpResponse = client.post(Const.API_URL + Const.API_TASK) {
//            header("Authorization", "Bearer $accessToken")
////            headers {
////                //append("Authorization", "Bearer $accessToken")
////                //append("Authorization", "Bearer Q")
////            }
//            //contentType(ContentType.Application.FormUrlEncoded)
//            contentType(ContentType.Application.Json)
//            setBody(taskRequest)
//        }

//        var uri = selectedFiles?.get(0)?.platformFile.toString()
//        var fileBytes: ByteArray? = null
//
//        context.contentResolver.openInputStream(Uri.parse(uri))?.use { inputStream ->
//            // Читаем файл в байты
//            fileBytes = inputStream.readBytes()
//
//            if (fileBytes!!.isNotEmpty()) {
//            } else {
//                println("Файл пустой: $uri")
//            }
//        } ?: throw Exception("Не удалось открыть файл: $uri")
//
//
//        println("@@@@@"+ (fileBytes?.size ?: ";;;"))


        //Нужно повторить чтото типа но с моими файлами
        //curl -X POST "http://10.0.20.179:90/megaplan/upload" -H "Accept: application/json" -H "Content-Type: multipart/form-data" -F "files=@C:\Users\reznichenko.i\Desktop\1.txt" -F "files=@C:\Users\reznichenko.i\Desktop\1.txt" -F "files=@C:\Users\reznichenko.i\Desktop\1.txt"


        val FILE_KEY = "files"

        val response: HttpResponse = client.post("${Const.API_URL}${Const.API_UPLOAD}") {
            setBody(
                MultiPartFormDataContent(
                    formData {
//                        append(
//                            key = "json",
//                            value = kotlinx.serialization.json.Json.encodeToString(TaskRequest.serializer(),taskRequest),
//                            headers = Headers.build {
//                                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
//                            }
//                        )

//                        append("json", kotlinx.serialization.json.Json.encodeToString(TaskRequest.serializer(),taskRequest), Headers.build {
//                            append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
//                            //append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
//                        })


                        append("name", taskRequest.name)
                        append("subject", taskRequest.subject)
                        append("contentType", "Task")
                        append("isTemplate", "false")
                        append("isUrgent", "false")
                        append("responsibleContentType", "Employee")
                        taskRequest.responsible?.let { append("responsibleId", it.id) }

//                        ///
//                        selectedFiles?.forEach { file ->
//                            val uri = file.platformFile.toString() // URI файла
//                            val fileName = file.path.substringAfterLast('/') // Имя файла
//
//                            // Создаем новый поток при каждом вызове appendInput
//                            context.contentResolver.openInputStream(Uri.parse(uri))?.use { inputStream ->
//                                appendInput(
//                                    key = "$FILE_KEY", // Ключ массива файлов
//                                    headers = Headers.build {
//                                        append(
//                                            HttpHeaders.ContentDisposition,
//                                            "filename=\"$fileName\""
//                                        )
//                                    },
//                                    size = inputStream.available().toLong() // Размер файла в Long
//                                ) {
//                                    inputStream.asInput() // Преобразуем InputStream в Input
//                                }
//                            } ?: throw Exception("Не удалось открыть файл: $uri")
//                        }
//                        ///


                        ///

//                        selectedFiles?.forEach { file ->
//                            val uri = file.platformFile.toString() // URI файла
//                            val fileName = file.path.substringAfterLast('/') // Имя файла
//                            context.contentResolver.openInputStream(Uri.parse(uri))?.use { inputStream ->
//                                appendInput(
//                                    key = "$FILE_KEY",
//                                    headers = Headers.build {
//                                        append(HttpHeaders.ContentDisposition,  "filename=\"$fileName\"")
//                                    }
//                                    //, size = file.length()
//                                ) {
//                                    inputStream.asInput()
//                                }
//                            } ?: throw Exception("Не удалось открыть файл: $uri")
//                        }

                        ///

                        selectedFiles?.forEach { file ->
                            val rawUri = file.platformFile.toString()
                            val uri = Uri.parse(rawUri)
                            val fileName = UriUtils.getFileName(context, uri) ?: "unknown_file"

                            val inputStream = context.contentResolver.openInputStream(uri)
                                ?: throw Exception("Не удалось открыть файл: $uri")

                            try {


                                println("FileName: $file")
                                println("FileName: $fileName")
                                //println("FileName: $inputStream")

                                append(FILE_KEY, inputStream.readBytes(), Headers.build {
                                    append(HttpHeaders.ContentType, getMimeType(fileName))
                                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                                })

//                                append(
//                                    key = FILE_KEY, // Ключ массива файлов
//                                    headers = Headers.build {
//                                        append(
//                                            HttpHeaders.ContentDisposition,
//                                            "filename=\"$fileName\""
//                                        )
//                                    }
//                                    //size = null // Размер файла не указываем
//                                ) {
//                                    inputStream.asInput() // Передаем поток напрямую
//                                }
                            } finally {
                                // Закрываем поток вручную
                                //inputStream.close()
                            }
                        }

                        ///

                        if (photoUri != null && photoUri != Uri.EMPTY) {

                            val fileName = UriUtils.getFileName(context, photoUri) ?: "unknown_file"

                            val inputStream = context.contentResolver.openInputStream(photoUri)
                                ?: throw Exception("Не удалось открыть фото: $photoUri")

                            try {
                                println("FileName - Photo: $fileName")
                                //println("FileName: $inputStream")

                                append(FILE_KEY, inputStream.readBytes(), Headers.build {
                                    append(HttpHeaders.ContentType, getMimeType(fileName))
                                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                                })
//                                append(
//                                    key = FILE_KEY, // Ключ массива файлов
//                                    headers = Headers.build {
//                                        append(
//                                            HttpHeaders.ContentDisposition,
//                                            "filename=\"$fileName\""
//                                        )
//                                    }
//                                    //size = null // Размер файла не указываем
//                                ) {
//                                    inputStream.asInput() // Передаем поток напрямую
//                                }
                            } finally {
                                // Закрываем поток вручную
                                //inputStream.close()
                            }
                        }

                        ////
                        if (shareVideo != null && shareVideo != Uri.EMPTY) {

                            val videoFileName =
                                UriUtils.getFileName(context, shareVideo) ?: "unknown_file.mp4"

                            val inputStream = context.contentResolver.openInputStream(shareVideo)
                                ?: throw Exception("Не удалось открыть фото: $shareVideo")

                            try {
                                println("FileName - Photo: $videoFileName")
                                //println("FileName: $inputStream")
                                append(FILE_KEY, inputStream.readBytes(), Headers.build {
                                    append(HttpHeaders.ContentType, getMimeType(videoFileName))
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"$videoFileName\""
                                    )
                                })
//                                append(
//                                    key = FILE_KEY,
//                                    headers = Headers.build {
//                                        append(
//                                            HttpHeaders.ContentDisposition,
//                                            "filename=\"$videoFileName\""
//                                        )
//                                    }
//                                    //size = null // Размер файла не указываем
//                                ) {
//                                    inputStream.asInput() // Передаем поток напрямую
//                                }
                            } finally {
                                // Закрываем поток вручную
                                //inputStream.close()
                            }
                        }

                        ////

                    }
                )
            )
        }


//        val response: HttpResponse = client.submitFormWithBinaryData(
//            url = "http://10.0.20.179:90/megaplan/upload",
//            formData = formData {
//                selectedFiles?.forEach { file ->
//                    val raw = file.platformFile.toString() // URI файла
//                    val uri = Uri.parse(raw)
//                    val fileName = UriUtils.getFileName(context, uri)
//
//                    println(fileName)
//
//                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
//                        append(
//                            FILE_KEY,
//                            inputStream.asInput(),
//                            Headers.build {
//                                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
//                            }
//                        )
//                    } ?: throw Exception("Не удалось открыть файл: $uri")
//                }
//            }
//        ) {
//            // Добавляем необходимые заголовки
//            header(HttpHeaders.Accept, "application/json")
//        }
//
//        // Читаем ответ
//        println("Response status: ${response.status}")
//        println("Response body: ${response.body<String>()}")


//        var formData = formData {
//
////            append("document", file.readBytes(), Headers.build {
////                append(HttpHeaders.ContentDisposition, "filename=${file.name}")
////            })
//
//
//            context.contentResolver.openInputStream(Uri.parse(uri))?.use { inputStream ->
//                // Читаем файл в байты
//                val fileBytes = inputStream.readBytes()
//
//                if (fileBytes.isNotEmpty()) {
////                    append(
////                        key = FILE_KEY,
////                        value = fileBytes,
////                        headers = Headers.build {
////                            append(
////                                HttpHeaders.ContentDisposition,
////                                "form-data; name=\"files\"; filename=\"${file.path.substringAfterLast('/')}\""
////                            )
////                        }
////                    )
//
//                    append("file", fileBytes, Headers.build {
//                        append(HttpHeaders.ContentDisposition, "filename=1234.ext")
//                    })
//
//                } else {
//                    println("Файл пустой: $uri")
//                }
//            } ?: throw Exception("Не удалось открыть файл: $uri")
//        }
//
//        val response: HttpResponse = client.submitFormWithBinaryData("${Const.API_URL}${Const.API_UPLOAD}", formData) {
//            header("Authorization", "Bearer $accessToken")
//        }

//        val inputStream = ByteArrayInputStream(byteArrayOf(77, 78, 79))
//
//        val formData = formData {
//            append("String value", "My name is") // строковый параметр
////            append("Number value", 179) // числовой
////            append("Bytes value", byteArrayOf(12, 74, 98)) // набор байт
//            append("Input value", inputStream.asInput(), headersOf("Streamheader", "Streamheadervalue")) // поток и заголовки
//        }
//
//        val response: HttpResponse = client.submitFormWithBinaryData("${Const.API_URL}${Const.API_UPLOAD}", formData) {
//            header("Authorization", "Bearer $accessToken")
//        }


        return if (response.status.isSuccess()) {
            // Если статус успешный (200-299), десериализуем как AuthResponse
            try {
                println("@@@@@@@==>" + response.toString())
                val taskResponse: TaskResponse = response.body()
                if (taskResponse.meta.status == 200) {
                    Result.success(taskResponse)
                } else {
                    Result.success(taskResponse)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            // Если статус неуспешный, обрабатываем как ошибку
            try {

                val errorResponse: TaskErrorResponse = response.body()
                //var msg = errorResponse.meta.errors.get(0).message
                var msg = errorResponse.message

                if (BuildConfig.DEBUG) {
                    println(errorResponse.toString())
                }

                Result.failure(Exception(msg))
            } catch (e: Exception) {
                Result.failure(Exception("Unknown error occurred, status: ${response.status}, body: ${response.bodyAsText()}"))
            }
        }
    }

    fun getMimeType(fileName: String): String {
        val extension = fileName.substringAfterLast('.', "")
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            ?: "application/octet-stream"
    }
}
