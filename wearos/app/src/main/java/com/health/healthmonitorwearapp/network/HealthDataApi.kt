package com.health.healthmonitorwearapp.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface HealthDataApi {
    @POST("/metrics/health")
    fun sendHealthData(@Body data: HealthMetricsDto): Call<Void>

    @POST("/metrics/daily")
    fun sendDailyMetrics(@Body data: DailyMetricsDto): Call<Void>


}
