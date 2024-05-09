package com.example.weathercodechallenge.presentation

data class RelevantWeatherData(val main: String,
                               val description: String,
                               val temperature: Double,
                               val humidity: Int,
                               val cloudCoverage: Int,
                               val icon: String)


