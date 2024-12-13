package com.lds.quickdeal.ui.viewmodels

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.config.Const

import com.lds.quickdeal.network.AuthRepository
import com.lds.quickdeal.repository.SettingsRepository
import com.unboundid.ldap.sdk.LDAPConnection
import com.unboundid.ldap.sdk.LDAPException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject constructor(

    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val context: Context

) : ViewModel() {

    //var accessToken: String? = null
    var autologin: Boolean = false

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
        onResult: (Boolean) -> Unit
    ) {


        Thread {
            try {

                if (BuildConfig.DEBUG) {
                    println("AD: $username $password")
                }


                val connection = LDAPConnection(Const.ldapHost, Const.ldapPort)
                connection.bind(Const.domain + "\\$username", password)
                println("Successfully connected and authenticated!")
                settingsRepository.saveADCredential(username, password)

                // Если удалось подключиться, логин успешен
                Handler(Looper.getMainLooper()).post { onResult(true) }

//                try {
//                    connection.close()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }

            } catch (e: LDAPException) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post { onResult(false) }
            } catch (e: Exception) {
                println("Unexpected error: ${e.message}")
                e.printStackTrace()
            }
        }.start()
    }
}