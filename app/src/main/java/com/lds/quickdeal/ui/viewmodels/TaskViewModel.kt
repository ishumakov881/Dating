package com.lds.quickdeal.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.db.TaskDao
import com.lds.quickdeal.android.entity.TaskStatus
import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.megaplan.entity.TaskRequest
import com.lds.quickdeal.megaplan.entity.TaskResponse
import com.lds.quickdeal.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class TaskViewModel
@Inject constructor(
    private val taskRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle,
    private val context: Context,
    private val taskDao: TaskDao
) : ViewModel() {

    // Объект текущей задачи
    private val _currentTask = MutableLiveData<UploaderTask>()
    val currentTask: LiveData<UploaderTask> get() = _currentTask

//    private val _description = MutableLiveData<String>()
//    val description: LiveData<String> get() = _description
//
//    init {
//        _description.value = if (BuildConfig.DEBUG) "Test Description" else ""
//    }


    init {
        _currentTask.value = createEmptyTask()
    }

    fun updateDescription(newDescription: String) {
        val task = _currentTask.value ?: createEmptyTask()
        _currentTask.value = task.copy(subject = newDescription)
    }

    fun appendDescription(s: String) {
        val task = _currentTask.value ?: createEmptyTask()
        _currentTask.value = task.copy(subject = task.subject + s)
    }

    fun updateTaskName(newName: String) {
//        _currentTask.value?.let {
//            it.name = newName
//        }
        val task = _currentTask.value ?: createEmptyTask()
        _currentTask.value = task.copy(name = newName)
    }

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
                val response = taskRepository.createOrUpdateTask(
                    taskRequest, _currentTask.value!!._id, selectedFiles, photoUri, shareVideo
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

    private var isTaskLoaded = false

    fun setTaskForEditing(taskId: String) {
        if (!isTaskLoaded && taskId.isNotEmpty()) {
            viewModelScope.launch {
                if (taskId.isNotEmpty()) {
                    val task = taskDao.getTaskById(taskId)
                    _currentTask.value = task ?: createEmptyTask()
                } else {
                    _currentTask.value = createEmptyTask()
                }
            }
        }
    }

    private fun createEmptyTask(): UploaderTask {
        val currentDateTime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return UploaderTask(
            name = "Задача $currentDateTime",
            subject = if (BuildConfig.DEBUG) "Test Description" else "",
            isUrgent = false,
            status = TaskStatus.NONE,
            createdAt = "",
            updatedAt = "", megaplanId = ""
        )
    }

}
