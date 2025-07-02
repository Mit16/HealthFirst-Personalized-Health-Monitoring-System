package com.health.healthmonitorwearapp.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.health.healthmonitorwearapp.services.DataSyncManager

class MovementSensorManager(private val context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepCounter: Sensor? = null
    private var accelerometer: Sensor? = null

    private var lastLogTime = 0L
    private var lastStepTime = 0L
    private var stepCount = 0
    private var lastMovementTime = System.currentTimeMillis()

    fun startListening() {
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        stepCount = 0
        DataSyncManager.updateSteps(0)

        stepCounter?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("MovementSensor", "Step counter registered")
        } ?: Log.w("MovementSensor", "Step counter not available")

        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("MovementSensor", "Accelerometer registered")
        } ?: Log.w("MovementSensor", "Accelerometer not available")
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        Log.d("MovementSensor", "Sensors unregistered")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val now = System.currentTimeMillis()

        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            if (isSignificantMovement(x, y, z)) {
                lastMovementTime = now
            }

            val magnitude = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val threshold = 11f

            if (magnitude > threshold && now - lastStepTime > 500) {
                stepCount++
                lastStepTime = now
                Log.d("StepFallback", "Step detected. Total: $stepCount")
                DataSyncManager.updateSteps(stepCount)
            }

            if (now - lastLogTime > 2000) {
                lastLogTime = now
                DataSyncManager.updateAccelerometer(x, y, z)
            }
        }
    }

    fun isIdle(): Boolean = (System.currentTimeMillis() - lastMovementTime) > 10 * 60 * 1000

    private fun isSignificantMovement(x: Float, y: Float, z: Float): Boolean {
        val magnitude = Math.sqrt((x * x + y * y + z * z).toDouble())
        return magnitude > 1.5
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Optional
    }
}
