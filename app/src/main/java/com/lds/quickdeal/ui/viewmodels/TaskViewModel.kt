package com.lds.quickdeal.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.frazo.audio_services.player.AndroidAudioPlayer
import br.com.frazo.audio_services.player.AudioPlayer
import br.com.frazo.audio_services.player.AudioPlayerStatus
import br.com.frazo.audio_services.player.AudioPlayingData

import br.com.frazo.audio_services.recorder.AndroidAudioRecorder
import br.com.frazo.audio_services.recorder.AudioRecorder
import br.com.frazo.audio_services.recorder.AudioRecordingData
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.config.Const.Companion.DEFAULT_OWNERS_
import com.lds.quickdeal.android.db.ResponsibleWrapper
import com.lds.quickdeal.android.db.TaskDao


import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.megaplan.entity.TaskRequest
import com.lds.quickdeal.megaplan.entity.TaskResponse
import com.lds.quickdeal.megaplan.entity.TaskStatus

import com.lds.quickdeal.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
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


    private val _owners = MutableStateFlow(DEFAULT_OWNERS_)
    val owners: StateFlow<List<ResponsibleWrapper>> = _owners

    private val _selectedResponsible = MutableStateFlow<ResponsibleWrapper?>(null)
    val selectedResponsible: StateFlow<ResponsibleWrapper?> = _selectedResponsible


    //=============================================================
    private val _servers = MutableStateFlow(Const.SERVER_LIST)
    val servers: StateFlow<List<String>> = _servers

    private val _selectedServer = MutableStateFlow<String>("")
    val selectedServer: StateFlow<String> = _selectedServer


    fun updateResponsible(owner: ResponsibleWrapper) {
        _selectedResponsible.value = owner
    }

    enum class AudioNoteStatus {
        HAVE_TO_RECORD, CAN_PLAY
    }

    //initialize states and variables
    private var _audioRecordFlow = MutableStateFlow<List<AudioRecordingData>>(emptyList())
    val audioRecordFlow = _audioRecordFlow.asStateFlow()
    private var currentAudioFile: File? = null
    private var _audioNoteStatus = MutableStateFlow(AudioNoteStatus.HAVE_TO_RECORD)
    val audioStatus = _audioNoteStatus.asStateFlow()

    val audioRecorder: AudioRecorder = AndroidAudioRecorder(context, Dispatchers.IO)


    //Initialize your variables
    private var _audioNotePlayingData =
        MutableStateFlow(AudioPlayingData(AudioPlayerStatus.NOT_INITIALIZED, 0, 0))
    val audioNotePlayingData = _audioNotePlayingData.asStateFlow()
    var audioPlayer: AudioPlayer = AndroidAudioPlayer(context)


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
        _selectedServer.value= _servers.value[0]
    }

//    private fun loadOwners() {
//        viewModelScope.launch {
//            try {
//                val loadedOwners = taskRepository.getOwners()
//                _owners.value = loadedOwners
//                _selectedResponsible.value = loadedOwners.getOrNull(0) // Установить первого как выбранного
//
//                //@@@ сохранение в базу...
//
//            } catch (e: Exception) {
//                // Обработка ошибок
//                println("Ошибка загрузки: ${e.message}")
//            }
//        }
//    }

    fun updateDescription(newDescription: String) {
        val task = _currentTask.value ?: createEmptyTask()
        _currentTask.value = task.copy(subject = newDescription)
    }

    fun appendDescription(s: String) {
        val task = _currentTask.value ?: createEmptyTask()
        _currentTask.value = task.copy(subject = task.subject + s)
    }


    fun updateTitle(s: String) {
        val task = _currentTask.value ?: createEmptyTask()
        _currentTask.value = task.copy(name = s)
    }

    fun appendTitle(s: String) {
        val task = _currentTask.value ?: createEmptyTask()
        _currentTask.value = task.copy(name = task.name + s)
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
                    server = _selectedServer.value,
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
        if (!isTaskLoaded) {
            viewModelScope.launch {
                if (taskId.isNotEmpty()) {
                    val task = taskDao.getTaskById(taskId)
                    _currentTask.value = task ?: createEmptyTask()
                } else {
                    _currentTask.value = createEmptyTask()
                }
                try {
                    val loadedOwners = taskRepository.getOwners(server = _selectedServer.value)
                    _owners.value = loadedOwners

                    _selectedResponsible.value = loadedOwners.find {
                        it.megaplanUserId == _currentTask.value?.responsibleId
                    } ?: loadedOwners.firstOrNull()
                    //_selectedResponsible.value = loadedOwners.getOrNull(0) // Установить первого как выбранного

                    //@@@ сохранение в базу...

                } catch (e: Exception) {
                    // Обработка ошибок
                    println("Ошибка загрузки: ${e.message}")
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
            updatedAt = "",
            megaplanId = "",
            responsibleId = ""
        )
    }


    //recorder
    //After you got the permission and the user clicks the record button, onRecordRequested is called...
    fun startRecordingAudioNote() {
        val audioDirectory = context.getExternalFilesDir(null)?.let {
            File(it, "audio_notes")
        } ?: File(context.filesDir, "audio_notes")
        if (!audioDirectory.exists()) {
            audioDirectory.mkdirs()
        }

        viewModelScope.launch {
            _audioRecordFlow.value = emptyList()
            currentAudioFile?.delete()
            currentAudioFile = File(audioDirectory, UUID.randomUUID().toString())
            currentAudioFile?.let { fileOutput ->
                val flow =
                    audioRecorder.startRecording(fileOutput)
                flow.catch {
                    audioRecorder.stopRecording()
                    fileOutput.delete()
                    currentAudioFile = null
                    //Do something with the error
                }
                    .collectLatest {
                        if (_audioRecordFlow.value.size >= 1000)
                            _audioRecordFlow.value =
                                _audioRecordFlow.value - _audioRecordFlow.value.first()
                        _audioRecordFlow.value = _audioRecordFlow.value + it
                    }
            }
        }
    }


    fun stopRecordingAudio() {
        audioRecorder.stopRecording()
        currentAudioFile?.let {
            println("Audio file: $currentAudioFile")
            _audioNoteStatus.value = AudioNoteStatus.CAN_PLAY
        }
    }


    //=================
    fun playAudioNote() {
        if (_audioNotePlayingData.value.status == AudioPlayerStatus.NOT_INITIALIZED) {
            currentAudioFile?.let { file ->
                viewModelScope.launch {
//                    val flow = audioPlayer.start(file)
//                    flow.catch {
//                        _uiState.value = UIState.Error(it)
//                        mediator.broadcast(
//                            uiParticipantRepresentative,
//                            UIEvent.Error(
//                                TextResource.RuntimeString(
//                                    it.localizedMessage ?: it.message ?: "An error has occurred."
//                                )
//                            )
//                        )
//                        audioPlayer.stop()
//                    }.collectLatest {
//                        _audioNotePlayingData.value = it
//                    }
                }
            }
        } else {
            resumeAudioNote()
        }
    }

    fun pauseAudioNote() {
        audioPlayer.pause()
    }

    private fun resumeAudioNote() {
        audioPlayer.resume()
    }

    fun deleteAudioNote() {
        audioPlayer.stop()
        currentAudioFile?.delete()
        _audioNoteStatus.value = AudioNoteStatus.HAVE_TO_RECORD
    }

    fun setServer(server: String) {
        _selectedServer.value = server
    }
}
