package com.example.weathercodechallenge.common

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weathercodechallenge.MyApp
import kotlinx.coroutines.flow.singleOrNull

/**
 * provides functions for interacting with preferences datastore
 */
object PrefsManager {

    private val USER_PREFERENCES_NAME = "user_preferences"  // datastore name

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    /**
     * make this a suspend fn as it may block UI
     */
    suspend fun readFromDatastore(key: String, ctx: Context): String? {
        val datastoreKey = stringPreferencesKey(key)
        val prefs = ctx.dataStore.data.singleOrNull()     // capture first emission of flow
        prefs?.let{
            return it[datastoreKey]
        }
        return null
    }

    /**
     * make this a suspend fn as it may block UI
     */
    suspend fun saveToDatastore(key: String, value: String, ctx: Context){
        val datastoreKey = stringPreferencesKey(key)
        ctx.dataStore.edit{
            it[datastoreKey] = value
        }
    }
}