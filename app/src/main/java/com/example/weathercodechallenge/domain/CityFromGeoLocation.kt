package com.example.weathercodechallenge.domain

import android.util.Log
import com.example.weathercodechallenge.MyApp
import com.example.weathercodechallenge.model.CityFromReverseGeoItem
import com.example.weathercodechallenge.repository.INetworkApi2
import com.example.weathercodechallenge.repository.SecureStorage
import retrofit2.Response
import javax.inject.Inject


/**
 * business use case for deriving local city name from Geo Coordinates
 */
class CityFromGeoLocation @Inject constructor(private val apiService: INetworkApi2,
                                              private val context: MyApp)
{

    private val TAG = "CityFromGeoLocation"

    /**
     * use geo-coordinates to fetch local city name
     *
     * @param   lat     latitude
     * @param   lon     longitude
     * @return  string representing name of local city
     */
    suspend operator fun invoke(lat: String,
                                lon: String,
                                limit: Int): List<CityFromReverseGeoItem>? {

        val apiKey: String? = SecureStorage.getApiKey(context)

        apiKey?.let { id ->

            return try {
                val response: Response<List<CityFromReverseGeoItem>> = apiService.getCityByReverseGeoLookup(lat, lon, limit, id)
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