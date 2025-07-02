package com.health.healthmonitorwearapp.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.health.healthmonitorwearapp.services.DataSyncManager

class BodyTemperatureSensorManager(
    private val context: Context
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var temperatureSensor: Sensor? = null

    fun startListening() {
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (temperatureSensor == null) {
            Log.w("TempSensor", "Body temperature sensor not available.")
            return
        }

        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
        Log.d("TempSensor", "Temperature sensor registered.")
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        Log.d("TempSensor", "Temperature sensor unregistered.")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            val temperature = event.values.firstOrNull() ?: return
            Log.d("TempSensor", "Body temp: $temperature °C")
            DataSyncManager.updateTemperature(temperature)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Optional
    }
}
