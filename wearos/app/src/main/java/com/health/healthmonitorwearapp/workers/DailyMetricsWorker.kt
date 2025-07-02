// File: workers/DailyMetricsWorker.kt
package com.health.healthmonitorwearapp.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.health.healthmonitorwearapp.R
import com.health.healthmonitorwearapp.services.DataSyncManager

class DailyMetricsWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        return try {
            Log.d("DailyMetricsWorker", "Executing daily sync")
            DataSyncManager.syncDailyMetrics()
            showSyncNotification("Daily sync completed successfully!")
            Result.success()
        } catch (e: Exception) {
            Log.e("DailyMetricsWorker", "Sync failed", e)
            showSyncNotification("⚠️ Daily sync failed, will retry.")
            Result.retry() // ⏱️ Retry with exponential backoff
        }
    }

    private fun showSyncNotification(message: String) {
        val channelId = "daily_sync_channel"

        // 🔔 Create notification channel on Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Daily Sync Status",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for daily sync status"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Customize as needed
            .setContentTitle("Health Sync")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // ✅ Safe check for Android 13+ runtime permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(1001, notification)
        } else {
            Log.w("DailyMetricsWorker", "Notification permission not granted, skipping notification.")
        }
    }

}
