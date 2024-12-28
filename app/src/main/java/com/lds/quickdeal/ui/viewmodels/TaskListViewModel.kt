package com.lds.quickdeal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lds.quickdeal.android.db.TaskDao
import com.lds.quickdeal.android.entity.UploaderTask
import com.lds.quickdeal.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val repository: TaskRepository

) : ViewModel() {
    private val _tasks = MutableLiveData<List<UploaderTask>>()
    val tasks: LiveData<List<UploaderTask>> get() = _tasks


    var localRepo = true


//    init {
//        loadTasks()
//    }

    fun loadTasks() {
        viewModelScope.launch {
           if(localRepo){
               _tasks.value = taskDao.getAllTasks()
           }else{
//               val result = repository.getAllTasks(server = _selectedServer.value)
//               if (result.isSuccess) {
//                   _tasks.postValue(result.getOrDefault(emptyList()))
//               } else {
//                   println(result.exceptionOrNull()?.message ?: "Unknown error")
//                   //_error.postValue(result.exceptionOrNull()?.message ?: "Unknown error")
//               }
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
                "name" -> compareBy { it.name }
                "subject" -> compareBy { it.subject }
                "createdAt" -> compareBy { it.createdAt }
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
            taskDao.delete(currentItem)

            _tasks.value = _tasks.value?.filter { it._id != currentItem._id }
        }
    }
}