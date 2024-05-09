package com.example.weathercodechallenge.repository

import com.example.weathercodechallenge.model.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface INetworkApi {

    @GET("?q={city}&appid={appID}")
    suspend fun getWeatherByCity(
        @Path("city") city: String,
        @Path("appid") apiKey: String): Response<WeatherData>

    @GET("?q={city},{cc}&appid={appID}")
    suspend fun getWeatherByCityCountry(
        @Path("city") city: String,
        @Path("cc") countryCode: String,
        @Path("appid") apiKey: String): Response<WeatherData>

    @GET("?q={city},{state},{cc}&appid={appID}")
    suspend fun getWeatherByCityStateCountry(
        @Path("city") city: String,
        @Path("state") state: String,
        @Path("cc") countryCode: String,
        @Path("appid") apiKey: String): Response<WeatherData>



    /* companion object {

        fun create() : INetworkApi {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(INetworkApi::class.java)

        }
    } */
}