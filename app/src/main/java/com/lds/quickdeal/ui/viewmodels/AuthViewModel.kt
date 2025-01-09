package com.lds.quickdeal.ui.viewmodels

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.repository.LDAPAuthRepository
import com.lds.quickdeal.repository.MegaplanAuthRepository
import com.lds.quickdeal.repository.SettingsRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel
@Inject constructor(

    private val authRepository: MegaplanAuthRepository,
    private val settingsRepository: SettingsRepository,
    private val context: Context

) : ViewModel() {

    //var accessToken: String? = null
    var autologin: Boolean = false

    private var taskJob: Job? = null
    private var _isTaskRunning = MutableLiveData(false)
    val isTaskRunning: LiveData<Boolean> = _isTaskRunning

    init {
        //val prefs = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE)
        //accessToken = prefs.getString(PREF_KEY_MEGAPLAN_ACCESS_TOKEN, null)

        val username = settingsRepository.getADuserName()
        val password = settingsRepository.getADPassword()

        autologin = username.isNotEmpty() && password.isNotEmpty()

        println("$username $password $autologin")
    }


    //Active Directory
    fun performAdLogin(
        context: Context,
        username: String,
        password: String,

        onError: (String?) -> Unit,
        onSuccess: (String?) -> Unit

    ) {

        taskJob = viewModelScope.launch {


            if (username.isEmpty()) {
                onError("Поле 'Логин' не должно быть пустым")
                _isTaskRunning.postValue(false)
                return@launch
            }

            if (password.isEmpty()) {
                onError("Поле 'Пароль' не должно быть пустым")
                _isTaskRunning.postValue(false)
                return@launch
            }
            _isTaskRunning.postValue(true)
            if (BuildConfig.DEBUG) {
                println("AD: $username $password")
            }

            val result = LDAPAuthRepository().login(username, password)
            when {
                result.isSuccess -> {
                    val data = result.getOrNull() // Получаем успешные данные (если есть)
                    println("Successfully connected and authenticated!")
                    settingsRepository.saveADCredential(username, password)

                    // Если удалось подключиться, логин успешен
                    onSuccess(data)
                }
                result.isFailure -> {
                    val error = result.exceptionOrNull() // Получаем исключение
                    onError(error?.message ?: "Произошла ошибка")
                }
            }
            _isTaskRunning.postValue(false)
        }
    }

    fun cancelTask() {
        taskJob?.cancel()
        _isTaskRunning.postValue(false)
    }
}