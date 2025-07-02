package com.health.healthmonitorwearapp.services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.health.healthmonitorwearapp.R
import com.health.healthmonitorwearapp.sensors.BodyTemperatureSensorManager
import com.health.healthmonitorwearapp.sensors.HeartRateSensorManager
import com.health.healthmonitorwearapp.sensors.MovementSensorManager
import com.health.healthmonitorwearapp.storage.UserManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine


class HealthMonitorService : Service() {

    private lateinit var heartRateSensorManager: HeartRateSensorManager
    private lateinit var movementSensorManager: MovementSensorManager

    //    private lateinit var spO2SensorManager: SpO2SensorManager
    private lateinit var bodyTemperatureSensorManager: BodyTemperatureSensorManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()
        heartRateSensorManager = HeartRateSensorManager(this)
        movementSensorManager = MovementSensorManager(this)
        bodyTemperatureSensorManager = BodyTemperatureSensorManager(this)
//        spO2SensorManager = SpO2SensorManager(this)
        startForeground(NOTIFICATION_ID, createNotification())

        observeMetricsAndUpdateNotification()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (!UserManager.isUserLinked(this)) {
            stopSelf()
            return START_NOT_STICKY
        }

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Health Monitoring Active")
            .setContentText("Reading heart rate data...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
        try {
            heartRateSensorManager.startListening()
            movementSensorManager.startListening()
//        spO2SensorManager.startListening()
            bodyTemperatureSensorManager.startListening()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return START_STICKY
    }

    private fun createNotification(): Notification {
        val content = """
        HR: ${DataSyncManager.getHeartRate()} bpm
        Temp: ${DataSyncManager.getBodyTemp()} °C
        Steps: ${DataSyncManager.getStepCount()} steps
        Sleep: ${DataSyncManager.sleepStatus.value}
    """.trimIndent()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Health Monitor Active")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "HealthMonitorServiceChannel"
        const val NOTIFICATION_ID = 101

        private var instance: HealthMonitorService? = null

        fun updateNotification() {
            instance?.let {
                val notification = it.createNotification()
                it.startForeground(NOTIFICATION_ID, notification)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        try {
            heartRateSensorManager.stopListening()
            movementSensorManager.stopListening()
//        spO2SensorManager.stopListening()
            bodyTemperatureSensorManager.stopListening()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Health Monitor Background Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun observeMetricsAndUpdateNotification() {
        serviceScope.launch {
            combine(
                DataSyncManager.heartRate,
                DataSyncManager.steps,
                DataSyncManager.sleepStatus,
            ) { heartRate, steps, sleep ->
                Triple(heartRate, steps, sleep)
            }.collectLatest { (heartRate, steps, sleep) ->
                val notification = createNotificationFromValues(heartRate, steps, sleep)
                startForeground(NOTIFICATION_ID, notification)
            }
        }
    }

    private fun createNotificationFromValues(
        heartRate: Float,
        steps: Int,
        sleep: String
    ): Notification {
        val content = """
        HR: ${heartRate.toInt()} bpm
        Temp: ${DataSyncManager.getBodyTemp()} °C
        Steps: $steps
        Sleep: $sleep
    """.trimIndent()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Health Monitor Active")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }


}
