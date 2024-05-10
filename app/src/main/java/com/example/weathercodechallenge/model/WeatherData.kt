package com.example.weathercodechallenge.model

import com.google.gson.annotations.SerializedName

data class WeatherData(
    val base: String,

    val clouds: Clouds,

    val cod: Int,

    @SerializedName("coord")
    val geo: Geo,

    val dt: Int,

    val id: Int,

    val main: Physics,

    val name: String,

    val rain: Rain,

    val sys: Sys,

    val timezone: Int,

    val visibility: Int,

    val weather: List<Weather>,

    val wind: Wind
)

data class Physics(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Clouds(
    val all: Int
)

data class Geo(
    val lat: Double,
    val lon: Double
)

data class Rain(
    val `1h`: Double
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)