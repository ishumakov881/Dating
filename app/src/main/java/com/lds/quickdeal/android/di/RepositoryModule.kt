package com.lds.quickdeal.android.di

import com.lds.quickdeal.network.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

//    @Provides
//    @Singleton
//    fun provideAuthRepository(client: HttpClient): AuthRepository {
//        return AuthRepository(client)
//    }
}
