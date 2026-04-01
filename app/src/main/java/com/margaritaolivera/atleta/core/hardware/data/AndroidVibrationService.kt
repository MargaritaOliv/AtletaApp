package com.margaritaolivera.atleta.core.hardware.data

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.margaritaolivera.atleta.core.hardware.domain.VibrationService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidVibrationService @Inject constructor(
    @ApplicationContext private val context: Context
) : VibrationService {

    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun vibrateShort() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }
    }

    override fun vibrateLong() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }
    }

    override fun vibrateError() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timings = longArrayOf(0, 100, 100, 100)
            val amplitudes = intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        } else {
            vibrator.vibrate(longArrayOf(0, 100, 100, 100), -1)
        }
    }
}
