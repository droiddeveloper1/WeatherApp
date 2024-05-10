package com.example.weathercodechallenge.repository

import com.example.weathercodechallenge.model.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface INetworkApi {

    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city:String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String): Response<WeatherData>

    @GET("weather")
    suspend fun getWeatherByCityCountry(
        @Query("q") cityCountryCode: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String): Response<WeatherData>

    @GET("weather")
    suspend fun getWeatherByCityStateCountry(
        @Query("q") cityStateCountryCode: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String): Response<WeatherData>
}