package com.health.healthmonitorwearapp.network

data class DailyMetricsDto(
    val userId: String,
    val steps: Int,
    val sleepStartTime: Long?,   // optional but can be useful
    val sleepEndTime: Long?,     // optional
    val sleepDuration: Double?,    // primary
    val date: String
)
