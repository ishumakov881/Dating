package com.lds.quickdeal.repository

import android.content.Context
import android.content.SharedPreferences
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.config.SettingsPreferencesKeys

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject



class SettingsRepository @Inject constructor(
    private val context: Context // Передаем Context для доступа к SharedPreferences
) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE)

    // Метод для получения настроек
    fun getSettings(): Settings {

        var username =
            sharedPreferences.getString(SettingsPreferencesKeys.MEGAPLAN_USERNAME, "") ?: ""
        if (username.isEmpty()) {
            val tmpName = getADuserName()
            if (tmpName != null) {
                if (tmpName.isNotEmpty()) {
                    username = tmpName
                }
            }
        }

        val password = getMegaPlanPassword()
        val accessToken =
            sharedPreferences.getString(SettingsPreferencesKeys.PREF_KEY_MEGAPLAN_ACCESS_TOKEN, "") ?: ""

        val expiresAt = sharedPreferences.getLong(SettingsPreferencesKeys.PREF_KEY_EXPIRES_AT, 0L)
        val expiresAtMillis = expiresAt * 1000 // Преобразуем в миллисекунды

        // Преобразуем метку времени в читаемую дату
        val formattedDate = if (expiresAt > 0L) {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            val date = Date(expiresAtMillis)
            dateFormat.format(date)
        } else {
            "Дата не установлена"
        }
        return Settings(
            megaplanUsername = username, megaplanPassword = password,
            accessToken = accessToken, expiresIn = formattedDate
        )
    }

//    suspend fun saveSettings(
//        username: String,
//        password: String
//    ) {
//        var oldUserName = getMegaPlanUserName()
//        var oldPassword = getMegaPlanPassword()
//
////        if (password.isNotEmpty() || username.isNotEmpty() && (oldUserName != username || oldPassword != password)) {
////            error("-----")
////        }else{
////            error("$oldUserName $oldPassword :: $username $password")
////        }
//
//        remo
//    }

    // Метод для сохранения настроек
    fun saveSettings(
        username: String,
        password: String,
        accessToken: String,
        expiresIn: Long
    ) {

        val editor = sharedPreferences.edit()
        editor.putString(SettingsPreferencesKeys.MEGAPLAN_USERNAME, username)
        editor.putString(SettingsPreferencesKeys.MEGAPLAN_PASSWORD, password)
        editor.putString(SettingsPreferencesKeys.PREF_KEY_MEGAPLAN_ACCESS_TOKEN, accessToken)
        //editor.putLong(PREF_KEY_EXPIRES_AT, System.currentTimeMillis() / 1000 + expiresIn)
        editor.putLong(SettingsPreferencesKeys.PREF_KEY_EXPIRES_AT, expiresIn)
        editor.apply()
    }

    fun getMegaPlanPassword(): String {
        return sharedPreferences.getString(SettingsPreferencesKeys.MEGAPLAN_PASSWORD, "") ?: ""
    }


    //AD
    fun saveADCredential(username: String, password: String): Unit {
        val editor = sharedPreferences.edit()
        editor.putString(SettingsPreferencesKeys.AD_USERNAME, username)//Reuse same username

        var mpUserName = getMegaPlanUserName()

        if (mpUserName.isEmpty()) {
            editor.putString(SettingsPreferencesKeys.MEGAPLAN_USERNAME, username)
        }


        editor.putString(SettingsPreferencesKeys.AD_PASSWORD, password)
        editor.apply()
    }

    fun getMegaPlanUserName(): String {
        return sharedPreferences.getString(SettingsPreferencesKeys.MEGAPLAN_USERNAME, "") ?: ""
    }

    fun getADuserName(): String {
        return sharedPreferences.getString(SettingsPreferencesKeys.AD_USERNAME, "") ?: ""
    }

    fun getADPassword(): String {
        return sharedPreferences.getString(SettingsPreferencesKeys.AD_PASSWORD, "") ?: ""
    }

}
