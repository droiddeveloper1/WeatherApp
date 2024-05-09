package com.example.weathercodechallenge.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecureStorage {

    private const val SHARED_PREF_FILE = "encrypted_shared_prefs"
    private const val API_KEY_PREF = "api_key"

    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            SHARED_PREF_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun storeApiKey(context: Context, apiKey: String) {
        getEncryptedSharedPreferences(context).edit().putString(API_KEY_PREF, apiKey).apply()
    }

    fun getApiKey(context: Context): String? {
        return getEncryptedSharedPreferences(context).getString(API_KEY_PREF, null)
    }
}