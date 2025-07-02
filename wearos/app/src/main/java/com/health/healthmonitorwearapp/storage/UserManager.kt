package com.health.healthmonitorwearapp.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object UserManager {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"

    fun saveUserId(context: Context, userId: String) {
        val prefs = getSecurePrefs(context)
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    private fun getEncryptedPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveLinkedUser(context: Context?, userId: String, userName: String) {
        context?.let {
            val prefs = getEncryptedPrefs(it)
            prefs.edit().putString(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, userName)
                .apply()
        }
    }

    fun getUserId(context: Context): String? {
        return getEncryptedPrefs(context).getString(KEY_USER_ID, null)
    }

    fun getUserName(context: Context): String? {
        return getEncryptedPrefs(context).getString(KEY_USER_NAME, null)
    }

    fun clearUserData(context: Context) {
        getEncryptedPrefs(context).edit().clear().apply()
    }

    fun isUserLinked(context: Context): Boolean {
        return getUserId(context) != null
    }

    fun clear(context: Context) {
        getSecurePrefs(context).edit().clear().apply()
    }

    private fun getSecurePrefs(context: Context) =
        EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
}
