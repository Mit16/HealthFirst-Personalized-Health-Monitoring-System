package com.health.healthmonitorwearapp.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Call

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/wear/link-wear-device")
    fun linkWearDevice(@Body tokenBody: Map<String, String>): Call<UserLinkResponse>
}