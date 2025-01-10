package com.lds.quickdeal.repository

import android.content.Context
import android.content.SharedPreferences
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.config.SettingsPreferencesKeys

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class SettingsRepository @Inject constructor(
    private val context: Context // Передаем Context для доступа к SharedPreferences
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE)

    // Метод для получения настроек
    fun getSettings(): Settings {

        var username =
            prefs.getString(SettingsPreferencesKeys.MEGAPLAN_USERNAME, "") ?: ""
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
            prefs.getString(SettingsPreferencesKeys.PREF_KEY_MEGAPLAN_ACCESS_TOKEN, "") ?: ""

        val expiresAt = prefs.getLong(SettingsPreferencesKeys.PREF_KEY_EXPIRES_AT, 0L)
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

        val editor = prefs.edit()
        editor.putString(SettingsPreferencesKeys.MEGAPLAN_USERNAME, username)
        editor.putString(SettingsPreferencesKeys.MEGAPLAN_PASSWORD, password)
        editor.putString(SettingsPreferencesKeys.PREF_KEY_MEGAPLAN_ACCESS_TOKEN, accessToken)
        //editor.putLong(PREF_KEY_EXPIRES_AT, System.currentTimeMillis() / 1000 + expiresIn)
        editor.putLong(SettingsPreferencesKeys.PREF_KEY_EXPIRES_AT, expiresIn)
        editor.apply()
    }

    fun getMegaPlanPassword(): String {
        return prefs.getString(SettingsPreferencesKeys.MEGAPLAN_PASSWORD, "") ?: ""
    }


    //AD
    fun saveADCredential(username: String, password: String, token: String): Unit {
        val editor = prefs.edit()
        editor.putString(SettingsPreferencesKeys.AD_USERNAME, username)
        editor.putString(SettingsPreferencesKeys.AD_TOKEN, token)


        var mpUserName = getMegaPlanUserName()

        if (mpUserName.isEmpty()) {//Reuse same username
            editor.putString(SettingsPreferencesKeys.MEGAPLAN_USERNAME, username)
        }

        editor.putString(SettingsPreferencesKeys.AD_PASSWORD, password)
        editor.apply()
    }

    fun getMegaPlanUserName(): String {
        return prefs.getString(SettingsPreferencesKeys.MEGAPLAN_USERNAME, "") ?: ""
    }

    fun getADuserName(): String {
        return prefs.getString(
            SettingsPreferencesKeys.AD_USERNAME,
            BuildConfig.ACTIVE_DIRECTORY_USERNAME
        ) ?: ""
    }

    fun getBearerToken(): String {
        return prefs.getString(SettingsPreferencesKeys.AD_TOKEN, null
        ) ?: ""
    }

    fun getADPassword(): String {
        return prefs.getString(
            SettingsPreferencesKeys.AD_PASSWORD,
            BuildConfig.ACTIVE_DIRECTORY_PASSWORD
        ) ?: ""
    }

    fun logout() {

        prefs.edit().apply {
            putString(SettingsPreferencesKeys.AD_USERNAME, "")
            putString(SettingsPreferencesKeys.AD_PASSWORD, "")
            putString(SettingsPreferencesKeys.AD_TOKEN, "")
            apply()
        }
    }

}
