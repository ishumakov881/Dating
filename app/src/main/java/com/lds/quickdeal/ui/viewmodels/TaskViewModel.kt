package com.lds.quickdeal.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.megaplan.entity.Owner
import com.lds.quickdeal.megaplan.entity.TaskRequest
import com.lds.quickdeal.megaplan.entity.TaskResponse
import com.lds.quickdeal.network.AuthResponse
import com.lds.quickdeal.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class TaskViewModel
@Inject constructor(
    private val taskRepository: TaskRepository, private val context: Context
) : ViewModel() {

    // Статус для отображения успешности операции
    var taskCreated = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    private var taskJob: Job? = null
    private var _isTaskRunning = MutableLiveData(false)
    val isTaskRunning: LiveData<Boolean> = _isTaskRunning

    // Функция для создания задачи
    fun createTask(
        taskRequest: TaskRequest,
        selectedFiles: List<MPFile<Any>>?,
        photoUri: Uri?,
        shareVideo: Uri?,
        onSuccess: (TaskResponse) -> Unit,
        onError: (String) -> Unit

    ) {
        taskJob = viewModelScope.launch {
            try {
                _isTaskRunning.postValue(true)
                val response = taskRepository.createTask(
                    taskRequest, selectedFiles, photoUri, shareVideo
                )
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
            } catch (e: CancellationException) {
                println("Coroutine cancelled: ${e.message}")
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Неизвестная ошибка"
                println("--> 0 -- Exception: $errorMessage")
                onError("$errorMessage")
            } finally {
                _isTaskRunning.postValue(false)
            }
        }
    }

    fun cancelTask() {
        taskJob?.cancel()
        _isTaskRunning.postValue(false)
    }
}
