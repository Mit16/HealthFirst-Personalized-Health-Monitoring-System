package com.health.healthmonitorwearapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.health.healthmonitorwearapp.services.DataSyncManager
import androidx.compose.ui.unit.sp // For fontSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@Composable
fun HealthStatusWidget() {
    val heartRate by DataSyncManager.heartRate.collectAsState()
    val steps by DataSyncManager.steps.collectAsState()
    val sleepStatus by DataSyncManager.sleepStatus.collectAsState()
    val bodyTemp = DataSyncManager.getBodyTemp()

    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("Heart Rate: $heartRate bpm", color = Color.White, fontSize = 14.sp, maxLines = 1)
        Text("Steps: $steps", color = Color.White, fontSize = 14.sp, maxLines = 1)
        Text("Sleep: $sleepStatus", color = Color.White, fontSize = 14.sp, maxLines = 1)
        Text("Temp: $bodyTemp °C", color = Color.White, fontSize = 14.sp, maxLines = 1)
    }
}
