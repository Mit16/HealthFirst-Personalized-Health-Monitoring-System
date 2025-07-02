package com.health.healthmonitorwearapp.network

data class HealthMetricsDto(
    val userId: String,
    val heartRate: Float,
    val accX: Float,
    val accY: Float,
    val accZ: Float,
    val spo2: Float,
    val bodyTemperature: Float,
    val timestamp: Long = System.currentTimeMillis()
)

