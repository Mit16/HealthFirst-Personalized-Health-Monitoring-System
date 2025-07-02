// JobScheduler.kt
package com.health.healthmonitorwearapp.work

import android.content.Context
import androidx.work.*
import com.health.healthmonitorwearapp.workers.DailyMetricsWorker
import java.util.*
import java.util.concurrent.TimeUnit

fun scheduleDailyMetricsSync(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val currentTime = Calendar.getInstance()
    val dueTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 1)
        set(Calendar.SECOND, 0)
        if (before(currentTime)) add(Calendar.DAY_OF_YEAR, 1)
    }

    val initialDelay = dueTime.timeInMillis - currentTime.timeInMillis

    val workRequest = PeriodicWorkRequestBuilder<DailyMetricsWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .addTag("daily-metrics-sync")
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "DailyMetricsSyncWork",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}
