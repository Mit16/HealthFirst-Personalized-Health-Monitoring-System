package com.health.healthmonitorwearapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.health.healthmonitorwearapp.work.scheduleDailyMetricsSync

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Device rebooted - rescheduling daily sync")
            context?.let { scheduleDailyMetricsSync(it) }
        }
    }
}
