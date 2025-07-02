package com.health.healthmonitorwearapp.services

import android.util.Log
import com.health.healthmonitorwearapp.network.DailyMetricsDto
import com.health.healthmonitorwearapp.network.HealthMetricsDto
import com.health.healthmonitorwearapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.health.healthmonitorwearapp.storage.SecureStorageManager
import com.google.gson.Gson
import android.content.Context
import com.health.healthmonitorwearapp.network.RetrofitClientAuth
import com.health.healthmonitorwearapp.network.UserLinkResponse
import com.health.healthmonitorwearapp.storage.EncryptedQueueManager
import com.health.healthmonitorwearapp.storage.QueueHelper.queueSafely
import com.health.healthmonitorwearapp.storage.UserManager



object DataSyncManager {

    private const val TAG = "WearLink"

    lateinit var appContext: Context
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val _heartRate = MutableStateFlow(0f)
    val heartRate: StateFlow<Float> = _heartRate

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

    private val _sleepStatus = MutableStateFlow("Awake")
    val sleepStatus: StateFlow<String> = _sleepStatus

    private val _bodyTemp = MutableStateFlow(0f)
    val bodyTemp: StateFlow<Float> = _bodyTemp

    private var accX = 0f
    private var accY = 0f
    private var accZ = 0f

    private var lastSyncTime = 0L
    private const val SYNC_INTERVAL_MS = 15_000L

    private var spo2: Float = 0f
    private var stepTimestamps: MutableList<Long> = mutableListOf()

    private var sleepStart: Long? = null
    private var sleepEnd: Long? = null
    private var sleepDuration: Long? = null


    // --- Public data accessors ---
    fun getSleepStart(): Long? = sleepStart
    fun getSleepEnd(): Long? = sleepEnd
    fun getSleepDuration(): Long? = sleepDuration
    fun getHeartRate(): Float = _heartRate.value
    fun getBodyTemp(): Float = _bodyTemp.value
    fun getStepCount(): Int = _steps.value
    fun getCadence(): Int = stepTimestamps.size

    // --- Step tracking ---
    fun updateSteps(value: Int) {
        _steps.value = value
        recordStepEvent()
    }

    fun recordStepEvent() {
        val now = System.currentTimeMillis()
        stepTimestamps.add(now)
        stepTimestamps = stepTimestamps.filter { now - it <= 60000 }.toMutableList()
    }

    // --- Sleep tracking ---
    fun startSleepTracking() {
        sleepStart = System.currentTimeMillis()
        sleepEnd = null
        sleepDuration = null
        _sleepStatus.value = "Sleeping"
    }

    fun stopSleepTracking() {
        sleepEnd = System.currentTimeMillis()
        sleepDuration = sleepEnd?.minus(sleepStart ?: 0)
        val mins = (sleepDuration ?: 0) / 60000
        _sleepStatus.value = "Slept $mins min"
    }

    // --- Metric updates ---
    fun updateHeartRate(value: Float) {
        _heartRate.value = value
        maybeSyncFrequentMetrics()
    }

    fun updateTemperature(value: Float) {
        _bodyTemp.value = value
        maybeSyncFrequentMetrics()
    }

    fun updateSpO2(value: Float) {
        spo2 = value
        maybeSyncFrequentMetrics()
    }

    fun updateAccelerometer(x: Float, y: Float, z: Float) {
        accX = x
        accY = y
        accZ = z
        maybeSyncFrequentMetrics()
    }


    // --- Frequent Sync ---
    private fun maybeSyncFrequentMetrics() {
        val now = System.currentTimeMillis()
        if (now - lastSyncTime < SYNC_INTERVAL_MS) return
        lastSyncTime = now

        val userId = UserManager.getUserId(appContext) ?: run {
            Log.w("DataSync", "User ID not linked. Aborting sync.")
            return
        }


        val payload = HealthMetricsDto(
            userId = userId,
            heartRate = _heartRate.value,
            accX = accX,
            accY = accY,
            accZ = accZ,
            spo2 = spo2,
            bodyTemperature = _bodyTemp.value,
            timestamp = now
        )

        RetrofitClient.instance.sendHealthData(payload)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("DataSync", "Frequent health data synced successfully")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("DataSync", "Failed to sync frequent data", t)
                    queueSafely(appContext, payload)
                }

            })

        HealthMonitorService.updateNotification()
    }

    // --- Daily Sync ---
    fun syncDailyMetrics() {
        val userId = UserManager.getUserId(appContext) ?: run {
            Log.w("DataSync", "User ID not linked. Aborting sync.")
            return
        }

        val sleepStartMillis = sleepStart?.takeIf { sleepDuration != null }
        val sleepEndMillis = sleepEnd?.takeIf { sleepDuration != null }

        // Convert millis to hours (rounded to 2 decimal places)
        val sleepDurationHours = sleepDuration?.let {
            (it.toDouble() / (1000 * 60 * 60)).let { hours -> String.format("%.2f", hours).toDouble() }
        }

        val payload = DailyMetricsDto(
            userId = userId,
            steps = _steps.value,
            sleepStartTime = sleepStartMillis,
            sleepEndTime = sleepEndMillis,
            sleepDuration = sleepDurationHours,
            date = getTodayDate()
        )

        RetrofitClient.instance.sendDailyMetrics(payload)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("DataSync", "✅ Daily metrics synced successfully")
                    SecureStorageManager.deleteLocalFile(appContext, "daily_metrics_cache")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("DataSync", "❌ Failed to sync daily metrics", t)
                    val json = Gson().toJson(payload)
                    val encrypted = SecureStorageManager.encrypt(json)
                    EncryptedQueueManager.enqueue(appContext, encrypted)
                }
            })
    }

    private fun getTodayDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(Date())
    }

    fun retryFailedFrequentSync(context: Context) {
        val cached = SecureStorageManager.readEncryptedFromFile(context, "frequent_health_cache")
        if (cached != null) {
            try {
                val dto = Gson().fromJson(cached, HealthMetricsDto::class.java)
                RetrofitClient.instance.sendHealthData(dto)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.d("DataSync", "🟢 Retried cached health data successfully")
                            SecureStorageManager.deleteLocalFile(context, "frequent_health_cache")
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("DataSync", "🔁 Retry failed", t)
                        }
                    })
            } catch (e: Exception) {
                Log.e("DataSync", "❌ Failed to parse cached payload", e)
            }
        }
    }

    fun retryFailedDailySync(context: Context) {
        val cached = SecureStorageManager.readEncryptedFromFile(context, "daily_metrics_cache")
        if (cached != null) {
            try {
                val dto = Gson().fromJson(cached, DailyMetricsDto::class.java)
                RetrofitClient.instance.sendDailyMetrics(dto)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.d("DataSync", "🟢 Retried daily sync successfully")
                            SecureStorageManager.deleteLocalFile(context, "daily_metrics_cache")
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("DataSync", "🔁 Retry of daily metrics failed", t)
                        }
                    })
            } catch (e: Exception) {
                Log.e("DataSync", "💥 Error parsing cached daily metrics", e)
            }
        }
    }
    fun retryQueuedPayloads() {
        val encryptedLines = EncryptedQueueManager.getAll(appContext)
        val successful = mutableListOf<String>()

        encryptedLines.forEach { encrypted ->
            try {
                val json = SecureStorageManager.decrypt(encrypted)

                if (json.contains("\"timestamp\"")) {
                    val dto = Gson().fromJson(json, HealthMetricsDto::class.java)
                    RetrofitClient.instance.sendHealthData(dto).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            synchronized(successful) {
                                successful.add(encrypted)
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("Retry", "Health data retry failed", t)
                        }
                    })
                } else {
                    val dto = Gson().fromJson(json, DailyMetricsDto::class.java)
                    RetrofitClient.instance.sendDailyMetrics(dto).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            synchronized(successful) {
                                successful.add(encrypted)
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("Retry", "Daily sync retry failed", t)
                        }
                    })
                }
            } catch (e: Exception) {
                Log.e("Retry", "Failed to decrypt or process queued payload", e)
            }
        }

        // Safely remove successful items and write remaining queue
        if (successful.isNotEmpty()) {
            val remaining = encryptedLines.filterNot { successful.contains(it) }
            val file = java.io.File(appContext.filesDir, "offline_health_queue.jsonl")

            synchronized(EncryptedQueueManager) {
                file.writeText(remaining.joinToString("\n"))
            }

            Log.d("Retry", "✅ Synced ${successful.size}, ${remaining.size} remain")
        }
    }

    fun linkWearDevice(
        token: String,
        context: Context? = null,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val body = mapOf("token" to token)
        Log.d(TAG, "Sending token: $token to backend")

        RetrofitClientAuth.instance.linkWearDevice(body).enqueue(object : Callback<UserLinkResponse> {
            override fun onResponse(
                call: Call<UserLinkResponse>,
                response: Response<UserLinkResponse>
            ) {
                Log.d(TAG, "Response received: code=${response.code()}, success=${response.isSuccessful}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.userId != null) {
                        Log.d(TAG, "User linked: ID=${responseBody.userId}, Name=${responseBody.userName}")

                        // Save userId locally (EncryptedSharedPreferences, etc.)
                        UserManager.saveLinkedUser(context, responseBody.userId, responseBody.userName)

                        context?.let {
                            Toast.makeText(it, "✅ Device linked to ${responseBody.userName}", Toast.LENGTH_SHORT).show()
                        }

                        onSuccess()
                    } else {
                        Log.w(TAG, "Response body is null or incomplete")
                        onError("Empty response from server")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown server error"
                    Log.e(TAG, "Link failed: $errorMsg")
                    onError("Failed to link device: ${response.code()} — $errorMsg")
                }
            }

            override fun onFailure(call: Call<UserLinkResponse>, t: Throwable) {
                Log.e(TAG, "Network error during linking: ${t.message}", t)
                onError("Network error: ${t.localizedMessage}")
            }
        })
    }


}
