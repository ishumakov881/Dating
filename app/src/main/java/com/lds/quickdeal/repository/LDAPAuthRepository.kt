//package com.lds.quickdeal.repository
//
//import android.os.Handler
//import android.os.Looper
//import com.lds.quickdeal.android.config.Const
//import com.unboundid.ldap.sdk.LDAPConnection
//import com.unboundid.ldap.sdk.LDAPException
//import com.unboundid.ldap.sdk.ResultCode
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//
//class LDAPAuthRepository : AuthRepository {
//
//
//    override suspend fun login(username: String, password: String): Result<String> {
//        return withContext(Dispatchers.IO) {
//            try {
//                //v1
//                val connection = LDAPConnection(Const.ldapHost, Const.ldapPort)
//
//                var authzID: String? = null
//
////                val mod = Modification(
////                    ModificationType.REPLACE,
////                    "description", "This is the new description."
////                )
////                val modifyRequest =
////                    ModifyRequest("dc=example,dc=com", mod)
////
////                val asyncRequestID =
////                    connection.asyncModify(modifyRequest, myAsyncResultListener)
////
////
////// Assume that we've waited a reasonable amount of time but the modify
////// hasn't completed yet so we'll try to cancel it.
////                var cancelResult = try {
////                    connection.processExtendedOperation(
////                        CancelExtendedRequest(asyncRequestID)
////                    )
////                    // This doesn't necessarily mean that the operation was successful, since
////                    // some kinds of extended operations (like cancel) return non-success
////                    // results under normal conditions.
////                } catch (le: LDAPException) {
////                    // For an extended operation, this generally means that a problem was
////                    // encountered while trying to send the request or read the result.
////                    ExtendedResult(le.toLDAPResult())
////                }
////
////                when (cancelResult.resultCode.intValue()) {
////                    ResultCode.CANCELED_INT_VALUE -> {}
////                    ResultCode.CANNOT_CANCEL_INT_VALUE -> {}
////                    ResultCode.TOO_LATE_INT_VALUE -> {}
////                    ResultCode.NO_SUCH_OPERATION_INT_VALUE -> {}
////                    else -> {}
////                }
////                ///00000000000000000
//
////                val bindRequest: BindRequest =
////                    SimpleBindRequest(
////                        "cn=$username,ou=users,dc=${Const.domain},dc=lds,dc=ua",//DN (Destination Name)
////                        password, AuthorizationIdentityRequestControl()
////                    )
////
////                val bindResult = connection.bind(bindRequest)
////                val authzIdentityResponse =
////                    AuthorizationIdentityResponseControl.get(bindResult)
////                if (authzIdentityResponse != null) {
////                    authzID = authzIdentityResponse.authorizationID
////                }
//                ///00000000000000000
//
//
////                    val baseDN = "dc=office,dc=lds,dc=ua"
////                    val userDN = "uid=$username,$baseDN"
//
//                // Пробуем выполнить bind (аутентификацию) с переданными данными
//                //connection.bind(userDN, password)
//
//                connection.bind(Const.domain + "\\$username", password)
////                val searchRequest = SearchRequest(
////                    "ou=Пользователи,dc=${Const.domain},dc=com", SearchScope.SUB, "(sAMAccountName=$username)")
////
////                try {
////                    val searchResult = connection.search(searchRequest)
////                    println("@@@ $searchResult")
////                    if (searchResult.searchEntries.isNotEmpty()) {
////                        val userEntry = searchResult.searchEntries[0]
////                        // Получение необходимых атрибутов
////                        val email = userEntry.getAttributeValue("mail")
////                        val fullName = userEntry.getAttributeValue("cn")
////
////                        println("@@@ $userEntry")
////                    }
////                }catch (e: Exception){
////                    println("====="+e)
////                }
//
//                // Если удалось подключиться, логин успешен
//                Result.success("")
//
////                try {
////                    connection.close()
////                } catch (e: Exception) {
////                    e.printStackTrace()
////                }
//
//            } catch (e: LDAPException) {
//                println(e.printStackTrace())
//
//
//                var result = e.resultString
//
//                println("www ${e.resultCode.intValue()} ${e.resultString}")
//
//                when (e.resultCode.intValue()) {
//                    ResultCode.REFERRAL_INT_VALUE -> {}
//                    ResultCode.CANCELED_INT_VALUE -> {}
//                    ResultCode.CANNOT_CANCEL_INT_VALUE -> {}
//                    ResultCode.TOO_LATE_INT_VALUE -> {}
//                    ResultCode.NO_SUCH_OPERATION_INT_VALUE -> {}
//
//                    //Произошла ошибка при попытке подключения к серверу
//                    //Не удалось установить соединение с сервером ldsi.office.lds.ua/10.0.20.2:389 в течение настроенного времени ожидания 10000 миллисекунд.
//                    ResultCode.CONNECT_ERROR_INT_VALUE -> {
//                        result = "Не удалось установить соединение с сервером"
//                    }
//
//                    ResultCode.INVALID_CREDENTIALS_INT_VALUE -> {
//                        result = "Пользователь с указанным логином не найден."
//                    }
//
//                    else -> {
//                        result = "Ошибка авторизации"
//                    }
//                }
//                Result.failure<String>(Exception(result))
//            } catch (e: Exception) {
//                println("Unexpected error: ${e.message}")
//                e.printStackTrace()
//                Result.failure<String>(e)
//            }
//        }
//
//    }
//}