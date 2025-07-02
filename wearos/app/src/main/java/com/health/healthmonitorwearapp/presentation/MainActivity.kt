package com.health.healthmonitorwearapp.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.*
import com.health.healthmonitorwearapp.R
import com.health.healthmonitorwearapp.presentation.theme.HealthMonitorWearAppTheme
import com.health.healthmonitorwearapp.services.HealthMonitorService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.health.healthmonitorwearapp.services.DataSyncManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.health.healthmonitorwearapp.services.HealthMonitoringService
import com.health.healthmonitorwearapp.ui.SleepSection
import com.health.healthmonitorwearapp.ui.HealthStatusWidget
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.health.healthmonitorwearapp.ui.SleepControlScreen
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.health.healthmonitorwearapp.storage.UserManager
import com.health.healthmonitorwearapp.ui.DebugSection
import com.health.healthmonitorwearapp.ui.PairingScreen
import com.health.healthmonitorwearapp.ui.SettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember


class MainActivity : ComponentActivity() {

    private val permissions = mutableListOf(
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.FOREGROUND_SERVICE_HEALTH
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        createNotificationChannel()

        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missing.toTypedArray(), 100)
        } else {
            if (!UserManager.isUserLinked(this)) {
                // Redirect to pairing screen or show a warning
                Log.w("MainActivity", "User not linked. Blocking services.")
                // e.g., navController.navigate("settings") or show a prompt
            } else {
                startHealthMonitorService()
                lifecycleScope.launch(Dispatchers.IO) {
                    DataSyncManager.syncDailyMetrics()
                }
            }
        }


        showMainUI()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                HealthMonitoringService.CHANNEL_ID,
                "Health Monitoring Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Tracks health data in background"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }



    private fun startHealthMonitorService() {
        Log.d("MainActivity", "Starting HealthMonitorService")
        val intent = Intent(this, HealthMonitorService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    private fun showMainUI() {
        Log.d("MainActivity", "Launching Wear UI")
        setContent {
            WearApp("Android")
        }
    }


    @Deprecated("Use ActivityResultContracts in modern apps")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            startHealthMonitorService()
        } else {
            Log.e("MainActivity", "Some permissions denied: $permissions")
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    val navController = rememberNavController()
    val scalingListState = rememberScalingLazyListState()

    val context = LocalContext.current
    val isLinked = remember { UserManager.isUserLinked(context) }

    HealthMonitorWearAppTheme {
        Scaffold(
            timeText = { TimeText() },
            vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
            positionIndicator = { PositionIndicator(scalingListState) }
        ) {
            NavHost(
                navController = navController,
                startDestination = if (isLinked) "main" else "pairing"
//                startDestination = "main"
            ) {
                composable("main") {
                    ScalingLazyColumn(
                        state = scalingListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item { HealthStatusWidget() }
                        item { SleepSection { navController.navigate("sleepControls") } }
                        item { DebugSection { navController.navigate("settings") } }
                    }
                }
                composable("pairing") {
                    PairingScreen(onSuccess = {
                        navController.navigate("main") {
                            popUpTo("pairing") { inclusive = true }
                        }
                    })
                }
                composable("sleepControls") {
                    SleepControlScreen()
                }
                composable("settings") {
                    SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun SleepLoggerUI() {
    Log.d("UIRender", "Rendering SleepLoggerUI")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            Log.d("SleepLogger", "Start button clicked")
            DataSyncManager.startSleepTracking()
        }) {
            Text("Start Sleep")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            Log.d("SleepLogger", "Stop button clicked")
            DataSyncManager.stopSleepTracking()
        }) {
            Text("Stop Sleep")
        }
    }
}

@Composable
fun HealthStatusWidget() {
    val heartRate = DataSyncManager.heartRate.collectAsState()
    val steps = DataSyncManager.steps.collectAsState()
    val sleepStatus = DataSyncManager.sleepStatus.collectAsState()
    val bodyTemp = DataSyncManager.bodyTemp.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Heart Rate: ${heartRate.value} bpm", color = Color.White)
        Text("Steps: ${steps.value}", color = Color.White)
        Text("Temp: ${bodyTemp.value} °C", color = Color.White, fontSize = 14.sp, maxLines = 1)
        Text("Sleep: ${sleepStatus.value}", color = Color.White)
    }
}


@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}
