package com.health.healthmonitorwearapp.storage

import android.content.Context
import android.util.Log
import java.io.File
import android.util.Base64

object EncryptedQueueManager {
    private const val QUEUE_FILE = "offline_health_queue.jsonl" // JSON Lines format

    fun enqueue(context: Context, encryptedPayload: String) {
        try {
            val file = File(context.filesDir, QUEUE_FILE)
            file.appendText(encryptedPayload + "\n")
            Log.d("EncryptedQueue", "✅ Payload queued")
        } catch (e: Exception) {
            Log.e("EncryptedQueue", "❌ Failed to queue payload", e)
        }
    }

    fun getAll(context: Context): List<String> {
        val file = File(context.filesDir, QUEUE_FILE)
        return if (file.exists()) {
            file.readLines()
                .filter { it.isNotBlank() && it.contains(":") }
                .filter {
                    val iv = it.split(":").getOrNull(0)?.let { ivPart ->
                        try {
                            Base64.decode(ivPart, Base64.DEFAULT).size == 16
                        } catch (e: Exception) {
                            false
                        }
                    } ?: false
                    iv
                }
        } else emptyList()
    }


    fun getAllSize(context: Context): Int {
        return getAll(context).size
    }

    fun clear(context: Context) {
        val file = File(context.filesDir, QUEUE_FILE)
        if (file.exists()) {
            file.writeText("")
            Log.d("EncryptedQueue", "🧹 Queue cleared")
        }
    }
}
