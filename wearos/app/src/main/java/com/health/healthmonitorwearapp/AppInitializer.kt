// AppInitializer.kt
package com.health.healthmonitorwearapp

import android.app.Application
import com.health.healthmonitorwearapp.services.DataSyncManager
import com.health.healthmonitorwearapp.work.scheduleDailyMetricsSync

class AppInitializer : Application() {
    override fun onCreate() {
        super.onCreate()
        scheduleDailyMetricsSync(applicationContext)
        DataSyncManager.init(this)
        DataSyncManager.retryFailedDailySync(applicationContext)
        DataSyncManager.retryFailedFrequentSync(applicationContext)
        DataSyncManager.retryQueuedPayloads()
    }
}
