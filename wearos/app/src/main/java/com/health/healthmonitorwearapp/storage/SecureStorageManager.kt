package com.health.healthmonitorwearapp.storage

import android.content.Context
import android.util.Base64
import android.util.Log
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object SecureStorageManager {

    private const val PASSWORD = "StrongPassword123!" // TODO: Replace with a config/constant
    private const val SALT = "StaticSalt1234"          // Static salt for now
    private const val ITERATION_COUNT = 65536
    private const val KEY_LENGTH = 256
    private const val FILE_NAME = "encrypted_health_log.json"

    private fun generateKey(): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(PASSWORD.toCharArray(), SALT.toByteArray(), ITERATION_COUNT, KEY_LENGTH)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    private fun generateIv(): IvParameterSpec {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return IvParameterSpec(iv)
    }

    fun encrypt(data: String): String {
        val key = generateKey()
        val iv = generateIv()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val encrypted = cipher.doFinal(data.toByteArray())

        val ivBase64 = Base64.encodeToString(iv.iv, Base64.DEFAULT)
        val encBase64 = Base64.encodeToString(encrypted, Base64.DEFAULT)

        return "$ivBase64:$encBase64"
    }

    fun decrypt(input: String): String {
        val parts = input.split(":")
        if (parts.size != 2) throw IllegalArgumentException("Invalid encrypted format: expected 2 parts separated by ':'")

        val iv = Base64.decode(parts[0], Base64.DEFAULT)
        if (iv.size != 16) throw IllegalArgumentException("Invalid IV size: expected 16 bytes but got ${iv.size}")

        val encrypted = Base64.decode(parts[1], Base64.DEFAULT)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, generateKey(), IvParameterSpec(iv))

        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted)
    }


    fun saveEncryptedToFile(context: Context, jsonData: String, fileName: String = FILE_NAME) {
        try {
            val encrypted = encrypt(jsonData)
            val file = File(context.filesDir, fileName)
            file.writeText(encrypted)
            Log.d("SecureStorage", "Encrypted data saved locally to $fileName")
        } catch (e: Exception) {
            Log.e("SecureStorage", "Error saving encrypted file", e)
        }
    }

    fun readEncryptedFromFile(context: Context, fileName: String = FILE_NAME): String? {
        return try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                val encrypted = file.readText()
                decrypt(encrypted)
            } else null
        } catch (e: Exception) {
            Log.e("SecureStorage", "Error reading encrypted file", e)
            null
        }
    }

    fun deleteLocalFile(context: Context, fileName: String = FILE_NAME) {
        val file = File(context.filesDir, fileName)
        if (file.exists()) {
            file.delete()
            Log.d("SecureStorage", "Encrypted file $fileName deleted.")
        }
    }

}
