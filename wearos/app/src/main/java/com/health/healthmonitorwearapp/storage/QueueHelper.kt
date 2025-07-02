package com.health.healthmonitorwearapp.storage

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.health.healthmonitorwearapp.storage.EncryptedQueueManager

object QueueHelper {
    fun queueSafely(context: Context, payload: Any) {
        try {
            val json = Gson().toJson(payload)
            val encrypted = SecureStorageManager.encrypt(json)
            EncryptedQueueManager.enqueue(context, encrypted)
        } catch (e: Exception) {
            Log.e("EncryptedQueue", "❌ Failed to encrypt and queue", e)
        }
    }
}
