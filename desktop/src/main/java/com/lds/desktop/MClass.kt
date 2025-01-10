package com.lds.desktop

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.runBlocking


object MClass {



    var SYSTEM_TOKEN =
        "ZTg4NzJkOGNhZjVhYjIxMjQxOWM3MTNkOTA0NTUxYzU0NDdkNWIwYWQyYTg0MWM1MDBmNGJmZTA2YmZlOWYyNw"
    var USER_TOKEN = "MWQ4ZmE5ZDVmYTQ3MDI4MTdiZDU4ZmIzNDhjZDZhMjQwN2E0Nzg0NWZmYTQ0N2VhZDA4ZDA5MDAwNTJjMGVhYQ"


    fun doAction(taskId: Int) = runBlocking {

        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }

        // создаем даные для POST запроса
        val requestBody = """
    {
        "contentType": "TaskActionRequest",
        "action": "act_accept_work"
    }
    """.trimIndent()

        try {
            // отправляем POST запрос
            val response: HttpResponse =
                client.post("https://megaplan.lds.online/api/v3/task/$taskId/doAction") {
                    header(
                        HttpHeaders.Authorization,
                        "Bearer $SYSTEM_TOKEN"
                    )
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
//                    header(
//                        HttpHeaders.Cookie,
//                        "ACCOUNTS_INFO=%7B%22accounts%22%3A%7B%22megaplan.lds.online%22%3A%7B%22lastActivityTime%22%3A1734008165%2C%22schemeAndHost%22%3A%22https%3A%5C%2F%5C%2Fmegaplan.lds.online%22%7D%7D%7D; ACCOUNT_URL=https%3A%2F%2Fmegaplan.lds.online; COOKIEID_BOX=1733984469_2jtlicgpqvoz1vcf8wch0; SID_BOX=1734605425_f4jgvupwi6uuo4lazmgi9"
//                    )
                    setBody(requestBody)
                }

            // обрабатываем ответ
            println("Response status: ${response.status}")
            println("Response body: ${response.bodyAsText()}")

        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
        } finally {
            // закрываем клиент
            client.close()
        }
    }


    @JvmStatic
    fun main(args: Array<String>) {
        println("Hello World")
        //doAction(11111)

        getAllTasks("Задача ")//test
    }

    private fun getAllTasks(s: String) = runBlocking {
        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }
        try {


            val response0: HttpResponse = client.get("https://megaplan.lds.online/api/v3/task") {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer $USER_TOKEN"
                    )
                    append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
//                    append(
//                        HttpHeaders.Cookie,
//                        "ACCOUNTS_INFO=%7B%22accounts%22%3A%7B%22megaplan.lds.online%22%3A%7B%22lastActivityTime%22%3A1734008165%2C%22schemeAndHost%22%3A%22https%3A%5C%2F%5C%2Fmegaplan.lds.online%22%7D%7D%7D; ACCOUNT_URL=https%3A%2F%2Fmegaplan.lds.online; COOKIEID_BOX=1733984469_2jtlicgpqvoz1vcf8wch0; SID_BOX=1734605425_f4jgvupwi6uuo4lazmgi9"
//                    )
                }
                setBody(
                    """{
                "q": "$s",
                "fields": "id"
            }"""
                )
            }

            println(response0.bodyAsText())
            val authResponse: TaskResponse = response0.body()
            authResponse.data?.forEach({

                if (it.name?.contains("test")!! || it.name?.contains("Test")!!|| it.name?.contains("Задача ")!!) {
                    println("${it.name} ${it.id}")
                    it.id?.let { it1 -> doAction(it1.toInt()) }
                }

            })
        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
        } finally {
            client.close()
        }

    }
}
