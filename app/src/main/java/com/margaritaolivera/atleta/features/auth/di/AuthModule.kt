package com.margaritaolivera.atleta.features.auth.di

import com.margaritaolivera.atleta.features.auth.data.remote.api.AuthApi
import com.margaritaolivera.atleta.features.auth.data.repositories.AuthRepositoryImpl
import com.margaritaolivera.atleta.features.auth.domain.repositories.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }
    }
}