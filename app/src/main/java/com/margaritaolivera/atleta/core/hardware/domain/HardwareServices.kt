package com.margaritaolivera.atleta.core.hardware.domain

interface VibrationService {
    fun vibrateShort()
    fun vibrateLong()
    fun vibrateError()
}

interface FlashService {
    fun toggleFlash(enabled: Boolean)
    fun blinkFlash(times: Int, intervalMs: Long = 100)
}

import kotlinx.coroutines.flow.Flow

data class AccelerometerData(
    val x: Float,
    val y: Float,
    val z: Float
)

interface AccelerometerService {
    fun startListening(): Flow<AccelerometerData>
    fun stopListening()
}
