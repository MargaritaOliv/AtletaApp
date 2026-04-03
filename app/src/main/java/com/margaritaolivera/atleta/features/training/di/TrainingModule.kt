package com.margaritaolivera.atleta.features.training.di

import com.margaritaolivera.atleta.features.training.data.remote.api.TrainingApi
import com.margaritaolivera.atleta.features.training.data.repositories.TrainingRepositoryImpl
import com.margaritaolivera.atleta.features.training.domain.repositories.TrainingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TrainingModule {

    @Binds
    @Singleton
    abstract fun bindTrainingRepository(impl: TrainingRepositoryImpl): TrainingRepository

    companion object {
        @Provides
        @Singleton
        fun provideTrainingApi(retrofit: Retrofit): TrainingApi {
            return retrofit.create(TrainingApi::class.java)
        }
    }
}