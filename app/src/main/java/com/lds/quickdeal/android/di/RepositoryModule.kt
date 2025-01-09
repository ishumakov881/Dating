package com.lds.quickdeal.android.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

//    @Provides
//    @Singleton
//    fun provideAuthRepository(client: HttpClient): AuthRepository {
//        return AuthRepository(client)
//    }
}
