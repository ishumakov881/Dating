package com.lds.quickdeal.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lds.quickdeal.network.AuthRepository
import com.lds.quickdeal.network.AuthResponse
import com.lds.quickdeal.repository.Settings
import com.lds.quickdeal.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository,
    private val context: Context

) : ViewModel() {

    fun getSettingsData(onSuccess: (Settings) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val settings = settingsRepository.getSettings()
                onSuccess(settings)
            } catch (e: Exception) {
                onError("Не удалось загрузить настройки: ${e.message}")
            }
        }
    }

    fun saveSettings(
        username: String,
        password: String,
        onSuccess: (Settings) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            var oldUserName = settingsRepository.getMegaPlanUserName()
            var oldPassword = settingsRepository.getMegaPlanPassword()




            try {
                if (password.isEmpty()) {
                    onError("Введите пароль")
                } else if (username.isEmpty()) {
                    onError("Введите логин")
                } else {

                    //var refreshToken = (oldUserName != username || oldPassword != password);
                    var refreshToken = true

                    if (refreshToken) {
                        megaplanLogin(username, password, {
                                authResponse->
                            Toast.makeText(context, "Токен обновлен -- $authResponse.accessToken}", Toast.LENGTH_LONG).show()
                            settingsRepository.saveSettings(username, password, authResponse.access_token, authResponse.expires_in)

                            val settings = settingsRepository.getSettings()
                            onSuccess(settings)
                        }, {
                                err->
                            onError("Ошибка авторизации: $err")
                        })
                    } else {
                        println("$oldUserName $oldPassword :: $username $password")
                    }
                }
            } catch (e: Exception) {
                onError("Ошибка при сохранении настроек: ${e.message}")
            }
        }
    }

    fun megaplanLogin(
        username: String,
        password: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = authRepository.getAccessToken(username, password)
                if (response.isSuccess) {
                    val authResponse = response.getOrThrow()
                    val accessToken = authResponse.access_token
                    authRepository.saveUserSession(username, authResponse)
                    onSuccess(authResponse)
                } else {
                    val error = response.exceptionOrNull()
                    val errorMessage = error?.message ?: "Неизвестная ошибка"
                    println("--> Error: $errorMessage")
                    onError("Ошибка авторизации: $errorMessage")
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Неизвестная ошибка"
                println("--> Exception: $errorMessage")
                onError("Ошибка авторизации: $errorMessage")
            }
        }
    }
}