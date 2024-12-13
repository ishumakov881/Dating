//package com.lds.quickdeal.android.di
//
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import io.ktor.client.HttpClient
//import io.ktor.client.engine.okhttp.OkHttp
//import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.client.plugins.logging.LogLevel
//import io.ktor.client.plugins.logging.Logging
//import io.ktor.serialization.gson.gson
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object NetworkModule {
//
//
//    @Provides
//    @Singleton
//    fun provideKtorClient(): HttpClient {
//        return HttpClient(OkHttp) {
//            install(ContentNegotiation) {
//                gson()
//            }
//            install(Logging) {
//                level = LogLevel.BODY
//            }
//        }
//    }
//
////    @Provides
////    @Singleton
////    fun provideHttpClient(): HttpClient {
////        return HttpClient(OkHttp) {
////            install(ContentNegotiation) {
////                gson()
////            }
////            install(Logging) {
////                level = LogLevel.BODY
////            }
////        }
////    }
//}