package com.lds.quickdeal.repository


import com.lds.quickdeal.android.config.Const
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class ServerAuthRepository : AuthRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
//        install(DefaultRequest) {
//            contentType(ContentType.Application.Json)
//        }
        }
    }

    override suspend fun login(username: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = client.post(Const.SERVER_PROD + "/ldap/login") {
                    setBody(
                        listOf(
                            "username" to username,
                            "password" to password
                        ).formUrlEncode()
                    )
                    contentType(ContentType.Application.FormUrlEncoded)
                }
                //println("@@@@@@@==>" + response.bodyAsText())

                if (response.status.isSuccess()) {
                    try {
                        val authResponse: AuthResponse = response.body()
                        //Result.success(authResponse)
                        Result.success(authResponse.access_token)

                    } catch (e: Exception) {
                        Result.failure(e)
                    }
                } else {

                    try {
                        val errorResponse: ErrorResponse1 = response.body()
                        Result.failure(Exception(errorResponse.message))
                    } catch (e: Exception) {
                        try {
                            val errorResponse: ErrorResponse2 = response.body()
                            Result.failure(Exception(errorResponse.message[0]))
                        } catch (e: Exception) {
                            Result.failure(Exception("Unknown error occurred, status: ${response.status}, body: ${response.bodyAsText()}"))
                        }
                    }
                }


            } catch (e: HttpRequestTimeoutException) {
                println("Превышено время ожидания запроса: ${e.message}")
                Result.failure(Exception("Превышено время ожидания запроса. Попробуйте позже"))
            } catch (e: Throwable) {
                handleException(e)
            }
        }
    }


}

@Serializable
data class AuthResponse(
    val access_token: String,
    val user: User
)

@Serializable
data class User(
    val username: String,
    val groups: List<String>
)

@Serializable
private data class ErrorResponse1(
    val statusCode: Int,
    val message: String,
    val error: String
)

@Serializable
private data class ErrorResponse2(
    val statusCode: Int,
    val message: List<String>,
    val error: String
)