package com.health.healthmonitorwearapp.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.health.healthmonitorwearapp.services.DataSyncManager // ✅ Make sure this exists!

class HeartRateSensorManager(
    private val context: Context
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private var heartRateSensor: Sensor? = null

    fun startListening() {
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        if (heartRateSensor == null) {
            Log.w("HeartRateSensor", "No heart rate sensor found.")
            return
        }

        Log.d("HeartRateSensor", "Registering heart rate sensor listener.")
        sensorManager.registerListener(
            this,
            heartRateSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        Log.d("HeartRateSensor", "Heart rate sensor listener unregistered.")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_HEART_RATE) {
            val heartRate = event.values.firstOrNull() ?: 0f
            Log.d("HeartRateSensor", "Heart rate detected: $heartRate")
            DataSyncManager.updateHeartRate(heartRate) // ✅ Now this will compile if DataSyncManager exists
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Optional
    }
}
