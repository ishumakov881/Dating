package com.lds.quickdeal.ui.viewmodels

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.config.Const
import com.lds.quickdeal.network.AuthRepository
import com.lds.quickdeal.repository.SettingsRepository
import com.unboundid.ldap.sdk.BindRequest
import com.unboundid.ldap.sdk.LDAPConnection
import com.unboundid.ldap.sdk.LDAPException
import com.unboundid.ldap.sdk.ResultCode
import com.unboundid.ldap.sdk.SearchRequest
import com.unboundid.ldap.sdk.SearchScope
import com.unboundid.ldap.sdk.SimpleBindRequest
import com.unboundid.ldap.sdk.controls.AuthorizationIdentityRequestControl
import com.unboundid.ldap.sdk.controls.AuthorizationIdentityResponseControl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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

            Thread {
                try {
                    _isTaskRunning.postValue(true)
                    if (BuildConfig.DEBUG) {
                        println("AD: $username $password")
                    }


                    val connection = LDAPConnection(Const.ldapHost, Const.ldapPort)
                    var authzID: String? = null

//                val mod = Modification(
//                    ModificationType.REPLACE,
//                    "description", "This is the new description."
//                )
//                val modifyRequest =
//                    ModifyRequest("dc=example,dc=com", mod)
//
//                val asyncRequestID =
//                    connection.asyncModify(modifyRequest, myAsyncResultListener)
//
//
//// Assume that we've waited a reasonable amount of time but the modify
//// hasn't completed yet so we'll try to cancel it.
//                var cancelResult = try {
//                    connection.processExtendedOperation(
//                        CancelExtendedRequest(asyncRequestID)
//                    )
//                    // This doesn't necessarily mean that the operation was successful, since
//                    // some kinds of extended operations (like cancel) return non-success
//                    // results under normal conditions.
//                } catch (le: LDAPException) {
//                    // For an extended operation, this generally means that a problem was
//                    // encountered while trying to send the request or read the result.
//                    ExtendedResult(le.toLDAPResult())
//                }
//
//                when (cancelResult.resultCode.intValue()) {
//                    ResultCode.CANCELED_INT_VALUE -> {}
//                    ResultCode.CANNOT_CANCEL_INT_VALUE -> {}
//                    ResultCode.TOO_LATE_INT_VALUE -> {}
//                    ResultCode.NO_SUCH_OPERATION_INT_VALUE -> {}
//                    else -> {}
//                }
//                ///00000000000000000

//                val bindRequest: BindRequest =
//                    SimpleBindRequest(
//                        "cn=$username,ou=users,dc=${Const.domain},dc=lds,dc=ua",//DN (Destination Name)
//                        password, AuthorizationIdentityRequestControl()
//                    )
//
//                val bindResult = connection.bind(bindRequest)
//                val authzIdentityResponse =
//                    AuthorizationIdentityResponseControl.get(bindResult)
//                if (authzIdentityResponse != null) {
//                    authzID = authzIdentityResponse.authorizationID
//                }
                    ///00000000000000000

                    connection.bind(Const.domain + "\\$username", password)
//                val searchRequest = SearchRequest(
//                    "ou=Пользователи,dc=${Const.domain},dc=com", SearchScope.SUB, "(sAMAccountName=$username)")
//
//                try {
//                    val searchResult = connection.search(searchRequest)
//                    println("@@@ $searchResult")
//                    if (searchResult.searchEntries.isNotEmpty()) {
//                        val userEntry = searchResult.searchEntries[0]
//                        // Получение необходимых атрибутов
//                        val email = userEntry.getAttributeValue("mail")
//                        val fullName = userEntry.getAttributeValue("cn")
//
//                        println("@@@ $userEntry")
//                    }
//                }catch (e: Exception){
//                    println("====="+e)
//                }

                    println("Successfully connected and authenticated! $authzID")
                    settingsRepository.saveADCredential(username, password)

                    // Если удалось подключиться, логин успешен
                    Handler(Looper.getMainLooper()).post { onSuccess(null) }

//                try {
//                    connection.close()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }

                } catch (e: LDAPException) {
                    println(e.printStackTrace())


                    var result = e.resultString

                    println("www ${e.resultCode.intValue()} ${e.resultString}")

                    when (e.resultCode.intValue()) {
                        ResultCode.REFERRAL_INT_VALUE -> {}
                        ResultCode.CANCELED_INT_VALUE -> {}
                        ResultCode.CANNOT_CANCEL_INT_VALUE -> {}
                        ResultCode.TOO_LATE_INT_VALUE -> {}
                        ResultCode.NO_SUCH_OPERATION_INT_VALUE -> {}

                        //Произошла ошибка при попытке подключения к серверу
                        //Не удалось установить соединение с сервером ldsi.office.lds.ua/10.0.20.2:389 в течение настроенного времени ожидания 10000 миллисекунд.
                        ResultCode.CONNECT_ERROR_INT_VALUE -> {
                            result = "Не удалось установить соединение с сервером"
                        }

                        ResultCode.INVALID_CREDENTIALS_INT_VALUE -> {
                            result = "Пользователь с указанным логином не найден."
                        }

                        else -> {
                            result = "Ошибка авторизации"
                        }
                    }
                    Handler(Looper.getMainLooper()).post { onError(result) }
                } catch (e: Exception) {
                    println("Unexpected error: ${e.message}")
                    e.printStackTrace()
                } finally {
                    _isTaskRunning.postValue(false)
                }
            }.start()
        }
    }

    fun cancelTask() {
        taskJob?.cancel()
        _isTaskRunning.postValue(false)
    }
}