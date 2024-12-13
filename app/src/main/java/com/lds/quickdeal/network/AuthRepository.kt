package com.lds.quickdeal.network

import android.content.Context
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.android.config.SettingsPreferencesKeys

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val client: HttpClient,
    private val context: Context
) {



//    suspend fun getAccessToken(username: String, password: String): AuthResponse {
//        return client.post(API_URL + "/api/v3/auth/access_token") {
//            contentType(ContentType.Application.FormUrlEncoded)
//            setBody(listOf(
//                "username" to username,
//                "password" to password,
//                "grant_type" to "password"
//            ).formUrlEncode())
//        }.body()
//    }


    suspend fun getAccessToken(megaplanUserName: String, password: String): Result<AuthResponse> {

        val prefs = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE)
        val tmpName = prefs.getString(SettingsPreferencesKeys.AD_USERNAME, null)

        if(megaplanUserName.isNotEmpty() && megaplanUserName == tmpName){
            val storedToken = prefs.getString(SettingsPreferencesKeys.PREF_KEY_MEGAPLAN_ACCESS_TOKEN, null)
            val storedRefreshToken = prefs.getString(SettingsPreferencesKeys.PREF_KEY_REFRESH_TOKEN, null)
            val storedTokenType = prefs.getString(SettingsPreferencesKeys.PREF_KEY_TOKEN_TYPE, null)
            val expiresAt = prefs.getLong(SettingsPreferencesKeys.PREF_KEY_EXPIRES_AT, 0L)
            val currentTime = System.currentTimeMillis() / 1000

            if (!storedToken.isNullOrEmpty() && expiresAt > currentTime) {
                // Если токен существует и ещё не истёк, возвращаем его.
                return Result.success(
                    AuthResponse(
                        access_token = storedToken,
                        refresh_token = storedRefreshToken.orEmpty(),
                        expires_in = (expiresAt - currentTime),
                        token_type = storedTokenType.orEmpty(),
                        scope = prefs.getString(SettingsPreferencesKeys.PREF_KEY_SCOPE, null)
                    )
                )
            }
        }

        // Выполняем запрос на сервер, если токена нет или он истёк.


        val response: HttpResponse = client.post(Const.API_URL + "/api/v3/auth/access_token") {
            setBody(
                listOf(
                    "username" to megaplanUserName,
                    "password" to password,
                    "grant_type" to "password"
                ).formUrlEncode()
            )
            contentType(ContentType.Application.FormUrlEncoded)
        }

        return if (response.status.isSuccess()) {
            // Если статус успешный (200-299), десериализуем как AuthResponse
            try {
                val authResponse: AuthResponse = response.body()
                Result.success(authResponse)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            // Если статус неуспешный, обрабатываем как ошибку
            try {
                val errorResponse: AuthErrorResponse = response.body()
                Result.failure(Exception("Error: ${errorResponse.error}, Description: ${errorResponse.error_description}"))
            } catch (e: Exception) {
                Result.failure(Exception("Unknown error occurred, status: ${response.status}, body: ${response.bodyAsText()}"))
            }
        }
    }


    fun saveUserSession(megaplanUserName: String, authResponse: AuthResponse) {
        val prefs = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(SettingsPreferencesKeys.MEGAPLAN_USERNAME, megaplanUserName)
            putString(SettingsPreferencesKeys.PREF_KEY_MEGAPLAN_ACCESS_TOKEN, authResponse.access_token)
            //putLong(PREF_KEY_EXPIRES_AT, System.currentTimeMillis() / 1000 + authResponse.expires_in)
            putLong(SettingsPreferencesKeys.PREF_KEY_EXPIRES_AT, authResponse.expires_in)

            putString(SettingsPreferencesKeys.PREF_KEY_TOKEN_TYPE, authResponse.token_type)
            putString(SettingsPreferencesKeys.PREF_KEY_SCOPE, authResponse.scope)
            putString(SettingsPreferencesKeys.PREF_KEY_REFRESH_TOKEN, authResponse.refresh_token)
            apply()
        }
    }
}