package com.lds.quickdeal.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.megaplan.entity.Owner
import com.lds.quickdeal.megaplan.entity.TaskResponse
import com.lds.quickdeal.network.AuthResponse
import com.lds.quickdeal.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class TaskViewModel
@Inject constructor(
    private val taskRepository: TaskRepository
    , private val context: Context
) : ViewModel() {

    // Статус для отображения успешности операции
    var taskCreated = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    // Функция для создания задачи
    fun createTask(topic: String, description: String, selectedFiles : List<MPFile<Any>>?
                   , photoUri: Uri?
                   , shareVideo: Uri?

                   , responsible: Owner,

                   onSuccess: (TaskResponse) -> Unit,
                   onError: (String) -> Unit

                   ) {
        viewModelScope.launch {
            try {
                val response = taskRepository.createTask(topic, description, selectedFiles
                    , photoUri
                    , shareVideo
                    , responsible)
                if (response.isSuccess) {
                    val taskResponse = response.getOrThrow()
                    taskCreated.value = true
                    onSuccess(taskResponse)
                } else {
                    errorMessage.value = "Ошибка при создании задачи"
                    val error = response.exceptionOrNull()
                    val errorMessage = error?.message ?: "Неизвестная ошибка"

                    println("--> Error: $errorMessage")
                    onError("Ошибка при создании задачи: $errorMessage")
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Неизвестная ошибка"
                println("--> Exception: $errorMessage")
                onError("$errorMessage")
            }
        }
    }
}
