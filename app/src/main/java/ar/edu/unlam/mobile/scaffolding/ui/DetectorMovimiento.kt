package ar.edu.unlam.mobile.scaffolding.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class DetectorMovimiento(
    private val context: Context,
    private val abrirCamera: () -> Unit,
) : SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var lastShakeTimestamp: Long = 0

    private val SHAKE_THRESHOLD = 12.0
    private val DEBOUNCE_TIME = 1000L

    fun start() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        accelerometer?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            detectShake(event)
        }
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int,
    ) {
        // vacio
    }

    private fun detectShake(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val magnitude = sqrt(x * x + y * y + z * z)

        if (magnitude > SHAKE_THRESHOLD) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastShakeTimestamp > DEBOUNCE_TIME) {
                lastShakeTimestamp = currentTime
                abrirCamera()
            }
        }
    }
}
