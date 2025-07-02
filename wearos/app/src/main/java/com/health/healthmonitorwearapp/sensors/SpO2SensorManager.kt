package com.health.healthmonitorwearapp.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.health.healthmonitorwearapp.services.DataSyncManager

class SpO2SensorManager(
    private val context: Context
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var spo2Sensor: Sensor? = null

    fun startListening() {
        // 34 is the unofficial constant for SpO2 on some devices
        val SPO2_SENSOR_TYPE = 34
        val availableSensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        spo2Sensor = availableSensors.find { it.type == SPO2_SENSOR_TYPE }

        if (spo2Sensor == null) {
            Log.w("SpO2Sensor", "SpO₂ sensor (type 34) not available on this device.")
            return
        }

        try {
            sensorManager.registerListener(this, spo2Sensor, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("SpO2Sensor", "SpO₂ sensor registered.")
        } catch (e: Exception) {
            Log.e("SpO2Sensor", "Failed to register SpO₂ sensor", e)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        Log.d("SpO2Sensor", "SpO₂ sensor unregistered.")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == 34) {
            val spo2 = event.values.firstOrNull()
            Log.d("SpO2Sensor", "SpO₂ detected: $spo2")
            DataSyncManager.updateSpO2(spo2 ?: 0f)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Optional
    }
}
