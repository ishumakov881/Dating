package com.lds.quickdeal.android.di

import android.content.Context
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.db.TaskDao
import com.lds.quickdeal.repository.MegaplanAuthRepository
import com.lds.quickdeal.repository.SettingsRepository
import com.lds.quickdeal.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson
import okhttp3.OkHttpClient
import java.net.InetSocketAddress
import java.net.Proxy
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideHttpClient(context: Context): HttpClient {

        val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("10.0.20.167", 8888))

        val okHttpClient = OkHttpClient.Builder()
            //.proxy(proxy)
            .build()

        return HttpClient(OkHttp) {

            defaultRequest {
                header(
                    "User-Agent",
                    "QuickDeal-" + context.packageName + "-" + BuildConfig.VERSION_NAME
                )
            }

//            install(ContentNegotiation) {
//                gson()
//            }
//            install(Logging) {
//                logger = Logger.DEFAULT
//                level = LogLevel.HEADERS
////                filter { request ->
////
////                    println(request.body.toString())
////                    request.url.host.contains("ktor.io")
////                }
//            }
//
//
////            install(HttpTimeout) {
////                requestTimeoutMillis = 1000_00
////            }
//            //install(Logging)

            engine {
                this.preconfigured = okHttpClient
            }
            install(ContentNegotiation) {
                gson()
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 600_000 // Таймаут запроса
                connectTimeoutMillis = 300_000 // Таймаут подключения
                socketTimeoutMillis = 300_000 // Таймаут сокета
            }
        }
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(c: Context): SettingsRepository {
        return SettingsRepository(c)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        context: Context,
        client: HttpClient,
        prefs: SettingsRepository
    ): MegaplanAuthRepository {
        return MegaplanAuthRepository(client, context, prefs)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        context: Context,
        client: HttpClient,
        dao: TaskDao,
        prefs: SettingsRepository
    ): TaskRepository {
        return TaskRepository(dao, client, context, prefs)
    }


}