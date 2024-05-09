package com.example.weathercodechallenge.common

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weathercodechallenge.MyApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull

/**
 * provides functions for interacting with preferences datastore.
 */
object PrefsManager {

    private val USER_PREFERENCES_NAME = "user_preferences"  // datastore name

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    /**
     * make this a suspend fn as it may block UI
     */
    suspend fun readFromDatastore(key: String): String? {
        val datastoreKey = stringPreferencesKey(key)
        val prefs = MyApp.appContext.dataStore.data.singleOrNull()     // capture first emission of flow
        prefs?.let{
            return it[datastoreKey]
        }
        return null
    }

    // Function to read a string from DataStore
    fun fetchFromDatastore(key: String): Flow<String?> {
        val datastoreKey = stringPreferencesKey(key)


        return MyApp.appContext.dataStore.data.map { preferences ->
                // Return the string or null if it doesn't exist
                preferences[datastoreKey] ?: ""
            }
    }

    /**
     * make this a suspend fn as it may block UI
     */
    suspend fun saveToDatastore(key: String, value: String){
        val datastoreKey = stringPreferencesKey(key)
        MyApp.appContext.dataStore.edit{
            it[datastoreKey] = value
        }
    }
}