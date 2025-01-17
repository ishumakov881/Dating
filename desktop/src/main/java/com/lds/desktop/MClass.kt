//package com.lds.desktop
//
//import io.ktor.client.*
//import io.ktor.client.call.body
//import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//
//import io.ktor.client.request.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.serialization.gson.gson
//import kotlinx.coroutines.runBlocking
//
//
//object MClass {
//
//
//
//    var SYSTEM_TOKEN =
//        "ODdmMGNiMGQwMWE2NTU1NTYxMGQ2MjRmZGNlNTdiZTJlMGMxY2IyMjQ1YjQxOTdmZDk0ODAxNWUxOWJkMmUzMQ"
//    var USER_TOKEN = "ODdmMGNiMGQwMWE2NTU1NTYxMGQ2MjRmZGNlNTdiZTJlMGMxY2IyMjQ1YjQxOTdmZDk0ODAxNWUxOWJkMmUzMQ"
//
//
//    fun doAction(taskId: Int) = runBlocking {
//
//        val client = HttpClient() {
//            install(ContentNegotiation) {
//                gson()
//            }
//        }
//
//        // создаем даные для POST запроса
//        val requestBody = """
//    {
//        "contentType": "TaskActionRequest",
//        "action": "act_accept_work"
//    }
//    """.trimIndent()
//
//        try {
//            // отправляем POST запрос
//            val response: HttpResponse =
//                client.post("https://megaplan.lds.online/api/v3/task/$taskId/doAction") {
//                    header(
//                        HttpHeaders.Authorization,
//                        "Bearer $SYSTEM_TOKEN"
//                    )
//                    header(HttpHeaders.ContentType, ContentType.Application.Json)
////                    header(
////                        HttpHeaders.Cookie,
////                        "ACCOUNTS_INFO=%7B%22accounts%22%3A%7B%22megaplan.lds.online%22%3A%7B%22lastActivityTime%22%3A1734008165%2C%22schemeAndHost%22%3A%22https%3A%5C%2F%5C%2Fmegaplan.lds.online%22%7D%7D%7D; ACCOUNT_URL=https%3A%2F%2Fmegaplan.lds.online; COOKIEID_BOX=1733984469_2jtlicgpqvoz1vcf8wch0; SID_BOX=1734605425_f4jgvupwi6uuo4lazmgi9"
////                    )
//                    setBody(requestBody)
//                }
//
//            // обрабатываем ответ
//            println("Response status: ${response.status}")
//            println("Response body: ${response.bodyAsText()}")
//
//        } catch (e: Exception) {
//            println("Error: ${e.localizedMessage}")
//        } finally {
//            // закрываем клиент
//            client.close()
//        }
//    }
//
//
//    @JvmStatic
//    fun main(args: Array<String>) {
//        println("Hello World")
//        //doAction(11111)
//
//        getAllTasks("куку")//test@Задача
//    }
//
//    private fun getAllTasks(s: String) = runBlocking {
//        val client = HttpClient() {
//            install(ContentNegotiation) {
//                gson()
//            }
//        }
//        try {
//
//
//            val response0: HttpResponse = client.get("https://megaplan.lds.online/api/v3/task") {
//                headers {
//                    append(
//                        HttpHeaders.Authorization,
//                        "Bearer $USER_TOKEN"
//                    )
//                    append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
////                    append(
////                        HttpHeaders.Cookie,
////                        "ACCOUNTS_INFO=%7B%22accounts%22%3A%7B%22megaplan.lds.online%22%3A%7B%22lastActivityTime%22%3A1734008165%2C%22schemeAndHost%22%3A%22https%3A%5C%2F%5C%2Fmegaplan.lds.online%22%7D%7D%7D; ACCOUNT_URL=https%3A%2F%2Fmegaplan.lds.online; COOKIEID_BOX=1733984469_2jtlicgpqvoz1vcf8wch0; SID_BOX=1734605425_f4jgvupwi6uuo4lazmgi9"
////                    )
//                }
//                setBody(
//                    """{
//                "q": "$s",
//                "fields": "id"
//            }"""
//                )
//            }
//
//
//
//            val authResponse: TaskResponse = response0.body()
//
//            authResponse.data?.forEach({
//
//
//
//                val keywords = listOf("test", "Test", "Задача ", "1111", "ббб", "eee", "Задача 20250109_080922", "Еще тест", "111-99999999999")
//
//                if (it.name?.let { name -> keywords.any { keyword -> name.contains(keyword) } } == true) {
//                    println("${it.name} ${it.owner}")
//                        it.id?.let { it1 -> doAction(it1.toInt()) }
//                }
//
//            })
//        } catch (e: Exception) {
//            println("Error: ${e.localizedMessage}")
//        } finally {
//            client.close()
//        }
//
//    }
//}
