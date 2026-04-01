package com.margaritaolivera.atleta.core.di

import com.margaritaolivera.atleta.core.hardware.data.AndroidAccelerometerService
import com.margaritaolivera.atleta.core.hardware.data.AndroidFlashService
import com.margaritaolivera.atleta.core.hardware.data.AndroidVibrationService
import com.margaritaolivera.atleta.core.hardware.domain.AccelerometerService
import com.margaritaolivera.atleta.core.hardware.domain.FlashService
import com.margaritaolivera.atleta.core.hardware.domain.VibrationService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Binds
    @Singleton
    abstract fun bindVibrationService(
        impl: AndroidVibrationService
    ): VibrationService

    @Binds
    @Singleton
    abstract fun bindFlashService(
        impl: AndroidFlashService
    ): FlashService

    @Binds
    @Singleton
    abstract fun bindAccelerometerService(
        impl: AndroidAccelerometerService
    ): AccelerometerService
}
