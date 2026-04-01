package com.margaritaolivera.atleta.core.hardware.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.margaritaolivera.atleta.core.hardware.domain.AccelerometerData
import com.margaritaolivera.atleta.core.hardware.domain.AccelerometerService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidAccelerometerService @Inject constructor(
    @ApplicationContext private val context: Context
) : AccelerometerService {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var listener: SensorEventListener? = null

    override fun startListening(): Flow<AccelerometerData> = callbackFlow {
        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    trySend(AccelerometerData(x, y, z))
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )

        awaitClose {
            stopListening()
        }
    }

    override fun stopListening() {
        listener?.let {
            sensorManager.unregisterListener(it)
            listener = null
        }
    }
}
