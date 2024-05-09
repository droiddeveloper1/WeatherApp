package com.example.weathercodechallenge.domain

import android.util.Log
import com.example.weathercodechallenge.MyApp
import com.example.weathercodechallenge.model.WeatherData
import com.example.weathercodechallenge.repository.INetworkApi
import com.example.weathercodechallenge.repository.SecureStorage
import retrofit2.Response
import javax.inject.Inject

/**
 * business use case for fetching weather data by CITY
 */
class WeatherFromCityUseCase @Inject constructor(private val apiService: INetworkApi,
                                                 private val context: MyApp)
{
    private val TAG = "WeatherFromCityStateCountryUseCase"

    suspend operator fun invoke(city: String): WeatherData? {

        val apiKey: String? = SecureStorage.getApiKey(context)

        apiKey?.let { id ->

            return try {
                val response: Response<WeatherData> = apiService.getWeatherByCity(city, id)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.w(TAG, "Unsuccessful response fetch.")
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "Error while fetching response.")
                null
            }
        } ?: run {
            return null
        }
    }
}