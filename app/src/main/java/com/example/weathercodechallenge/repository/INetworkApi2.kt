package com.example.weathercodechallenge.repository

import com.example.weathercodechallenge.model.CityFromReverseGeoItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface INetworkApi2 {

    @GET("reverse")
    suspend fun getCityByReverseGeoLookup(
        @Query("lat") latitude:String,
        @Query("lon") longitude:String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String): Response<List<CityFromReverseGeoItem>>
}