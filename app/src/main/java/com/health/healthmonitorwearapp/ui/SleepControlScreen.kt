package com.health.healthmonitorwearapp.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.health.healthmonitorwearapp.services.DataSyncManager
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@Composable
fun SleepControlScreen() {
    val sleepStatus by DataSyncManager.sleepStatus.collectAsState()
    val scalingListState = rememberScalingLazyListState()

    ScalingLazyColumn(
        state = scalingListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Sleep Status: $sleepStatus", color = Color.White, fontSize = 14.sp)
        }

        item {
            Button(
                onClick = {
                    Log.d("SleepLogger", "Start Sleep Clicked")
                    DataSyncManager.startSleepTracking()
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black)
            ) {
                Text("Start Sleep")
            }
        }

        item {
            Button(
                onClick = {
                    Log.d("SleepLogger", "Stop Sleep Clicked")
                    DataSyncManager.stopSleepTracking()
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black)
            ) {
                Text("Stop Sleep")
            }
        }
    }
}
