package com.health.healthmonitorwearapp.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.health.healthmonitorwearapp.R

class HealthMonitoringService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("HealthService", "Service created")

        // Start foreground with notification
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Health Monitoring Active")
            .setContentText("Collecting vital health metrics...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("HealthService", "Service started")
        // You can optionally initialize sensors here if needed
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("HealthService", "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "health_monitor_channel"
        const val NOTIFICATION_ID = 1001
    }
}
