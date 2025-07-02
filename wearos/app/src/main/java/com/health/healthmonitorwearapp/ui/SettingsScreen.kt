// File: ui/SettingsScreen.kt
package com.health.healthmonitorwearapp.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.health.healthmonitorwearapp.services.DataSyncManager
import com.health.healthmonitorwearapp.storage.EncryptedQueueManager
import com.health.healthmonitorwearapp.storage.UserManager

@Composable
fun SettingsScreen() {
    var queueSize by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        queueSize = EncryptedQueueManager.getAllSize(DataSyncManager.appContext)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🛠 Debug Settings", color = Color.White, fontSize = 16.sp)

        Text("Queued Logs: $queueSize", color = Color.White, fontSize = 14.sp)

        Button(
            onClick = {
                Log.d("Settings", "Retry button pressed")
                DataSyncManager.retryQueuedPayloads()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(44.dp)
        ) {
            Text("🔁 Retry Now")
        }

        // 🔓 Unlink Device button
        Button(
            onClick = {
                UserManager.clear(DataSyncManager.appContext)
                android.os.Process.killProcess(android.os.Process.myPid()) // Soft-restart app
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(44.dp)
        ) {
            Text("🔓 Unlink Device")
        }
    }
}
