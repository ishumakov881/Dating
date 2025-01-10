package com.lds.quickdeal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.db.TaskDao
import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val repository: TaskRepository

) : ViewModel() {
    private val _tasks = MutableLiveData<List<UploaderTask>>()
    val tasks: LiveData<List<UploaderTask>> get() = _tasks

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadTasks()

    }

    fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (Const.LOCAL_REPO) {
                    _tasks.value = taskDao.getAllTasks()
                } else {
                    val result = repository.getAllTasks(server = Const.SERVER_PROD)
                    if (result.isSuccess) {
                        //_tasks.postValue(result.getOrDefault(emptyList()))
                        _tasks.value = (result.getOrDefault(emptyList()))

                    } else {
                        println(result.exceptionOrNull()?.message ?: "Unknown error")
                        //_error.postValue(result.exceptionOrNull()?.message ?: "Unknown error")
                    }
                }
            } catch (e: Exception) {
                // Обработка ошибок
            } finally {
                _isLoading.value = false
            }
        }

    }

    fun clearTasks() {
        viewModelScope.launch {
            taskDao.deleteAllTasks()
            _tasks.value = emptyList() // Обновляем список задач
        }
    }

    fun sortTasks(selectedSortField: String) {
        _tasks.value = _tasks.value?.sortedWith(
            when (selectedSortField) {
                "id" -> compareBy {
                    if (Const.LOCAL_REPO) it._id else it.localId
                }
                "name" -> compareBy { it.name }
                "status" -> compareBy { it.status }
                "status_dsc" -> compareByDescending { it.status }

                "subject" -> compareBy { it.subject }
                "createdAt" -> compareByDescending { it.createdAt }
                "updatedAt"-> compareByDescending{it.updatedAt}
                else -> compareBy { it.megaplanId }
            }
        )
    }

    fun filterTasksByStatus(b: Boolean) {
        _tasks.value = _tasks.value?.sortedWith(
            compareBy { it.status }
        )
    }

    fun removeItem(currentItem: UploaderTask) {
        viewModelScope.launch {
            if (Const.LOCAL_REPO) {
                taskDao.delete(currentItem)
                _tasks.value = _tasks.value?.filter { it._id != currentItem._id }
            } else {
                _tasks.value = _tasks.value?.filter { it.localId != currentItem.localId }
            }

        }
    }
}