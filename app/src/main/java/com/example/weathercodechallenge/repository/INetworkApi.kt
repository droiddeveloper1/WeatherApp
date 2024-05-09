package com.example.weathercodechallenge.repository

import com.example.weathercodechallenge.model.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface INetworkApi {

    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city:String,
        @Query("appid") apiKey: String): Response<WeatherData>

    @GET("weather?q={city},{cc}&appid={appID}")
    suspend fun getWeatherByCityCountry(
        @Query("q") cityCountryCode: String,
        @Query("appid") apiKey: String): Response<WeatherData>

    @GET("weather?q={city},{state},{cc}&appid={appID}")
    suspend fun getWeatherByCityStateCountry(
        @Path("q") cityStateCountryCode: String,
        @Path("appid") apiKey: String): Response<WeatherData>

}