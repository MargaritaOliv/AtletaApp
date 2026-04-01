package com.margaritaolivera.atleta.core.hardware.data

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.Looper
import com.margaritaolivera.atleta.core.hardware.domain.FlashService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidFlashService @Inject constructor(
    @ApplicationContext private val context: Context
) : FlashService {

    private val cameraManager: CameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val handler = Handler(Looper.getMainLooper())

    override fun toggleFlash(enabled: Boolean) {
        try {
            val cameraId = cameraManager.cameraIdList.getOrNull(0) ?: return
            cameraManager.setTorchMode(cameraId, enabled)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun blinkFlash(times: Int, intervalMs: Long) {
        var count = 0
        val runnable = object : Runnable {
            var isOn = false
            override fun run() {
                if (count < times * 2) {
                    isOn = !isOn
                    toggleFlash(isOn)
                    count++
                    handler.postDelayed(this, intervalMs)
                } else {
                    toggleFlash(false)
                }
            }
        }
        handler.post(runnable)
    }
}
