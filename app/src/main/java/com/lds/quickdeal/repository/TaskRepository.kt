package com.lds.quickdeal.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.config.Const.Companion.FILE_KEY
import com.lds.quickdeal.android.db.ResponsibleWrapper
import com.lds.quickdeal.android.config.SettingsPreferencesKeys
import com.lds.quickdeal.android.config.SettingsPreferencesKeys.SettingsPreferencesKeys.PREF_KEY_MEGAPLAN_ACCESS_TOKEN
import com.lds.quickdeal.android.db.TaskDao
import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.android.utils.TaskUtils.Companion.appendTaskRequest
import com.lds.quickdeal.android.utils.TimeUtils
import com.lds.quickdeal.android.utils.UriUtils
import com.lds.quickdeal.megaplan.entity.Responsible

import com.lds.quickdeal.megaplan.entity.TaskRequest
import com.lds.quickdeal.megaplan.entity.TaskResponse


import com.lds.quickdeal.network.TaskErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.utils.io.streams.asInput
import kotlinx.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
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

//    suspend fun createTask(
//        taskRequest: TaskRequest,
//        selectedFiles: List<MPFile<Any>>?,
//        photoUri: Uri?,
//        shareVideo: Uri?,
//
//    ): Result<TaskResponse> {
//
//        return taskCreate(taskRequest, selectedFiles, photoUri, shareVideo)
//    }

    suspend fun createOrUpdateTask(
        taskRequest: TaskRequest,
        taskId: Long,
        selectedFiles: List<MPFile<Any>>?, photoUri: Uri?,
        shareVideo: Uri?

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
        val username = prefs.getString(SettingsPreferencesKeys.AD_USERNAME, null)

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


        try {
            val response: HttpResponse = client.post("${Const.API_URL}${Const.API_UPLOAD}") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            appendTaskRequest(context, taskRequest)
                            append("username", username ?: "")
                            selectedFiles?.forEach { file ->
                                val rawUri = file.platformFile.toString()
                                val uri = Uri.parse(rawUri)
                                val fileName = UriUtils.getFileName(context, uri) ?: "unknown_file"

                                val inputStream =
                                    context.contentResolver.openInputStream(uri) ?: throw Exception(
                                        "Не удалось открыть файл: $uri"
                                    )

                                try {
                                    println("FileName: $file $fileName")
                                    //println("FileName: $inputStream")
                                    //ver 1.0 OutOfMemoryError | inputStream.readBytes()
//                                    append(FILE_KEY, inputStream.readBytes(), Headers.build {
//                                        append(HttpHeaders.ContentType, getMimeType(fileName))
//                                        append(
//                                            HttpHeaders.ContentDisposition,
//                                            "filename=\"$fileName\""
//                                        )
//                                    })
                                    //ver 1.1
                                    val totalSize = UriUtils.getFileSize(context, uri)
                                    val progressInputStream = ProgressInputStream(
                                        inputStream,
                                        totalSize
                                    ) { bytesSent, totalBytes ->
                                        val progress =
                                            (bytesSent.toFloat() / totalBytes.toFloat()) * 100
                                        println("Uploaded: $fileName $bytesSent/$totalBytes (${progress.toInt()}%)")
                                    }
                                    appendInput(
                                        key = FILE_KEY, // Ключ массива файлов
                                        headers = Headers.build {
                                            append(
                                                HttpHeaders.ContentDisposition,
                                                "filename=\"$fileName\""
                                            )
                                        }
                                        //, size = inputStream.available().toLong() // Размер файла в Long
                                        , size = totalSize
                                    ) {
                                        progressInputStream.asInput()
                                    }

//                                    append(
//                                        key = FILE_KEY, // Ключ массива файлов
//                                        headers = Headers.build {
//                                            append(
//                                                HttpHeaders.ContentDisposition,
//                                                "filename=\"$fileName\""
//                                            )
//                                        }
//                                        //size = null // Размер файла не указываем
//                                    ) {
//                                        inputStream.asInput() // Передаем поток напрямую
//                                    }
                                } finally {
                                    //inputStream.close()
                                }
                            }

                            ///

                            if (photoUri != null && photoUri != Uri.EMPTY) {

                                val fileName =
                                    UriUtils.getFileName(context, photoUri) ?: "unknown_file"

                                val inputStream = context.contentResolver.openInputStream(photoUri)
                                    ?: throw Exception("Не удалось открыть фото: $photoUri")

                                try {
                                    println("FileName - Photo: $fileName")
                                    //println("FileName: $inputStream")

                                    val totalSize = UriUtils.getFileSize(context, photoUri)
                                    val progressInputStream = ProgressInputStream(
                                        inputStream,
                                        totalSize
                                    ) { bytesSent, totalBytes ->
                                        val progress =
                                            (bytesSent.toFloat() / totalBytes.toFloat()) * 100
                                        println("Uploaded: $fileName $bytesSent/$totalBytes (${progress.toInt()}%)")
                                    }

//v 1.0
//                                    append(
//                                        FILE_KEY,
//                                        progressInputStream.readBytes(),
//                                        Headers.build {
//                                            append(HttpHeaders.ContentType, getMimeType(fileName))
//                                            append(
//                                                HttpHeaders.ContentDisposition,
//                                                "filename=\"$fileName\""
//                                            )
//                                        })


                                    //v1.1
                                    appendInput(
                                        key = FILE_KEY,
                                        headers = Headers.build {
                                            append(HttpHeaders.ContentType, getMimeType(fileName))
                                            append(
                                                HttpHeaders.ContentDisposition,
                                                "filename=\"$fileName\""
                                            )
                                        }) {
                                        progressInputStream.asInput()
                                    }

//                                    original append(FILE_KEY, inputStream.readBytes(), Headers.build {
//                                        append(HttpHeaders.ContentType, getMimeType(fileName))
//                                        append(
//                                            HttpHeaders.ContentDisposition,
//                                            "filename=\"$fileName\""
//                                        )
//                                    })

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
                                    //inputStream.close()// Закрываем поток вручную
                                }
                            }

                            ////
                            if (shareVideo != null && shareVideo != Uri.EMPTY) {

                                val videoFileName =
                                    UriUtils.getFileName(context, shareVideo) ?: "unknown_file.mp4"

                                val inputStream =
                                    context.contentResolver.openInputStream(shareVideo)
                                        ?: throw Exception("Не удалось открыть фото: $shareVideo")

                                try {
                                    println("FileName - Video: $videoFileName")
                                    //println("FileName: $inputStream")


                                    //v 1.0
//                                    append(FILE_KEY, inputStream.readBytes(), Headers.build {
//                                        append(HttpHeaders.ContentType, getMimeType(videoFileName))
//                                        append(
//                                            HttpHeaders.ContentDisposition,
//                                            "filename=\"$videoFileName\""
//                                        )
//                                    })


                                    val totalSize = UriUtils.getFileSize(context, shareVideo)
                                    val progressInputStream = ProgressInputStream(
                                        inputStream,
                                        totalSize
                                    ) { bytesSent, totalBytes ->
                                        val progress =
                                            (bytesSent.toFloat() / totalBytes.toFloat()) * 100
                                        println("Uploaded: $videoFileName $bytesSent/$totalBytes (${progress.toInt()}%)")
                                    }

                                    //v1.1
                                    appendInput(
                                        key = FILE_KEY, headers = Headers.build {
                                            append(
                                                HttpHeaders.ContentType,
                                                getMimeType(videoFileName)
                                            )
                                            append(
                                                HttpHeaders.ContentDisposition,
                                                "filename=\"$videoFileName\""
                                            )
                                        }) {
                                        progressInputStream.asInput()
                                    }

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

            return if (response.status.isSuccess()) {
                // Если статус успешный (200-299), десериализуем как AuthResponse
                try {
                    println("@@@@@@@==>" + response.bodyAsText() + " " + taskRequest.name)
//                    val taskResponse: TaskResponse = response.body()
//                    if (taskResponse.meta.status == 200) {
//                        Result.success(taskResponse)
//                    } else {
//                        Result.success(taskResponse)
//                    }

                    //Тут должан быть код
                    //val taskResponse = TaskResponse(Meta(200, listOf()), listOf()) // Предположим, что мы получили TaskResponse

                    //Oleg server
                    val taskResponse: TaskResponse = response.body()


                    var responsibleId = taskRequest.responsible?.id ?: ""
                    println("@Request: ${taskRequest.megaplanId} | $responsibleId")


                    println("@@@@@@@@@@@@@@@ ${taskResponse.synced}  ${taskResponse.megaplanId}")
                    println("@@@@@@@@@@@@@@@ ${taskResponse.name}  ${taskResponse.subject}")


                    // Сохраняем задачу в базе данных, если ответ успешный
                    val createAtNow = TimeUtils.nowTimeFormatted()

                    val task = UploaderTask(
                        name = taskRequest.name,
                        subject = taskRequest.subject,
                        isUrgent = false,//????????????????
                        status = taskResponse.getStatus() // Можно добавить логику для задания статуса задачи
                        ,
                        createdAt = taskResponse.createdAt ?: createAtNow,
                        updatedAt = taskResponse.updatedAt ?: createAtNow,
                        megaplanId = taskResponse.megaplanId ?: "" // предполагаем, что ID задачи приходит с сервера
                        , responsibleId = responsibleId
                    )


                    if (taskRequest.megaplanId.isEmpty()) {
                        taskDao.insert(task)
                    } else {
                        task._id = taskId
                        task.megaplanId = taskRequest.megaplanId
                        taskDao.update(task)
                        println("isnew-> ${taskRequest.name} ${task._id} ${task.megaplanId}")
                        //taskDao.updateByMegaplanId(taskRequest.megaplanId, taskRequest.name,taskRequest.subject)
                        //taskDao.updateById(..., taskRequest.name,taskRequest.subject)
                    }
                    Result.success(taskResponse)


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
                        println("ОШИБКА СЕРВЕРА: " + errorResponse.toString())
                        println("ОШИБКА СЕРВЕРА: " + errorResponse.message)
                    }

                    Result.failure(Exception(msg))
                } catch (e: Exception) {
                    Result.failure(Exception("Unknown error occurred, status: ${response.status}, body: ${response.bodyAsText()}"))
                }
            }
        } catch (e: HttpRequestTimeoutException) {
            println("Превышено время ожидания запроса: ${e.message}")
            return Result.failure(Exception("Превышено время ожидания запроса. Попробуйте позже"))
        } catch (e: Throwable) {
            return handleException(e)
        }
    }

    fun getMimeType(fileName: String): String {
        val extension = fileName.substringAfterLast('.', "")
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            ?: "application/octet-stream"
    }

    suspend fun getAllTasks(): Result<List<UploaderTask>> {
        val prefs = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE)
        val username = prefs.getString(SettingsPreferencesKeys.AD_USERNAME, null)
        try {
            val response: HttpResponse =
                client.get("${Const.API_URL}/megaplan/task_list?username=$username")
            return if (response.status.isSuccess()) {

                try {


                    val tmp: List<TaskResponse> = response.body()
                    val uploaderTasks: List<UploaderTask> = tmp.map { taskResponse ->
                        println("${taskResponse.updatedAt} == ${taskResponse.createdAt}")
                        UploaderTask(
                            _id = 0,
                            name = taskResponse.name,
                            subject = taskResponse.subject,
                            isUrgent = taskResponse.isUrgent
                                ?: false, // Если isUrgent null, то false
                            createdAt = taskResponse.createdAt
                                ?: "", // Если createdAt null, то пустая строка
                            updatedAt = taskResponse.updatedAt
                                ?: "", // Если updatedAt null, то пустая строка
                            megaplanId = taskResponse.megaplanId,

                            responsibleId = taskResponse.responsible.id,
                            status = taskResponse.getStatus() // Преобразуем статус
                        )
                    }


                    //Result.success(taskResponse)
                    Result.success(uploaderTasks)


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
                        println("ОШИБКА СЕРВЕРА: " + errorResponse.toString())
                        println("ОШИБКА СЕРВЕРА: " + errorResponse.message)
                    }

                    Result.failure(Exception(msg))
                } catch (e: Exception) {
                    Result.failure(Exception("Unknown error occurred, status: ${response.status}, body: ${response.bodyAsText()}"))
                }
            }
        } catch (e: HttpRequestTimeoutException) {
            // Таймаут запроса
            println("Превышено время ожидания запроса: ${e.message}")
            return Result.failure(Exception("Превышено время ожидания запроса. Попробуйте позже"))
        } catch (e: Throwable) {
            return handleException(e)
        }

    }

    suspend fun getOwners(): List<ResponsibleWrapper> {
        return try {
            var response: HttpResponse = client.get("${Const.API_URL}/megaplan/users")

            return if (response.status.isSuccess()) {

                var responsible: List<Responsible> = response.body()
                val specialItems = defaultList()
//                responsible с такими данными не должен быть добавлен в список
////                                    "id": "1000093",
////                                    "name": "Система API",


                val mappedItems = responsible
                    .filter { it.id != Const.MEGAPLAN_SYSTEM_ID }
                    .sortedBy { it.name }
                    .map {

                        var tmp =
                            it.avatar?.thumbnail?.replace("{width}", "100")
                                ?.replace("{height}", "100")
//                    var tmp =
//                        it.avatar?.path

                        //println("avatar: $tmp")

                        ResponsibleWrapper(
                            _id = 0, // Room сам сгенерирует ID
                            contentType = "Employee",
                            megaplanUserId = it.id,
                            description = it.name,
                            avatar = if (tmp.isNullOrEmpty()) "" else Const.MEGAPLAN_URL + tmp,
                            position = it.position
                        )
                    }
                (specialItems + mappedItems).distinctBy { it.megaplanUserId }
            } else {
                defaultList()
            }
        } catch (e: Exception) {
            println("@@@@@" + e)
            defaultList()
        }
    }

    private fun defaultList(): List<ResponsibleWrapper> {
        return listOf(
            ResponsibleWrapper(
                _id = 0, // Room сам сгенерирует ID
                "Employee",
                megaplanUserId = "",
                description = "<Не выбрано>",
                avatar = "",
                position = ""
            ),
//            ResponsibleWrapper(
//                "Employee",
//                id = "1000093",
//                description = "Система API",
//                avatar = "",
//                position = ""
//            )
        )
    }

//    suspend fun getOwners(): List<EmployeeWrapper> {
//        // Симуляция загрузки данных с сервера
//        delay(10000) // Имитация задержки
//        return listOf(
//            ,
//            ,
//            EmployeeWrapper("Employee", "1000163", "Иванов Петр Сергеевич")
//        )
//    }
}


fun <T> handleException(e: Throwable): Result<T> {
    return when (e) {
        is CancellationException -> {
            println("Coroutine cancelled: ${e.message}")
            throw e // Обязательно перебросьте исключение дальше
        }

        is IOException -> {
            // Ошибки сети (например, отсутствие интернета)
            println("Ошибка сети: ${e.message} $e")
            Result.failure(Exception("Ошибка сети. Проверьте подключение к интернету"))
        }

        is ClientRequestException -> {
            // Клиентская ошибка (4xx)
            println("Клиентская ошибка: ${e.message}")
            Result.failure(Exception("Клиентская ошибка: ${e.response.status.description}"))
        }

        is ServerResponseException -> {
            // Серверная ошибка (5xx)
            println("Ошибка сервера: ${e.message}")
            Result.failure(Exception("Ошибка на стороне сервера: ${e.response.status.description}"))
        }

//        is HttpRequestTimeoutException -> {
//            // Таймаут запроса
//            println("Превышено время ожидания запроса: ${e.message}")
//            Result.failure(Exception("Превышено время ожидания запроса. Попробуйте позже"))
//        }

        is OutOfMemoryError -> {
            // Ошибка памяти
            println("FileLoad OOM при загрузке файла $e")
            Result.failure(Exception("Ошибка: недостаточно памяти для обработки файла"))
        }

        is Exception -> {
            // Обработка остальных исключений
            println("Неизвестная ошибка: ${e.javaClass}: ${e.message}")
            Result.failure(Exception("Произошла неизвестная ошибка"))
        }

        else -> {
            println("Неизвестная ошибка: ${e.javaClass}: ${e.message}")
            Result.failure(Exception("Произошла неизвестная ошибка"))
        }
    }
}
