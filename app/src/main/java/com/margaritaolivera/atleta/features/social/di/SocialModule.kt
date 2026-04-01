package com.margaritaolivera.atleta.features.social.di

import com.margaritaolivera.atleta.features.social.data.remote.api.SocialApi
import com.margaritaolivera.atleta.features.social.data.repositories.SocialRepositoryImpl
import com.margaritaolivera.atleta.features.social.domain.repositories.SocialRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocialModule {

    @Binds
    @Singleton
    abstract fun bindSocialRepository(impl: SocialRepositoryImpl): SocialRepository

    companion object {
        @Provides
        @Singleton
        fun provideSocialApi(retrofit: Retrofit): SocialApi {
            return retrofit.create(SocialApi::class.java)
        }
    }
}