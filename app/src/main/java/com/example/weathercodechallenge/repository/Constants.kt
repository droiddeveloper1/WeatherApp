package com.example.weathercodechallenge.repository


val BASE_DATA_URL = "https://api.openweathermap.org/data/2.5/"

val BASE_IMAGE_URL = "https://openweathermap.org/img/wn/"

val BASE_REVERSE_GEO_URL = "https://api.openweathermap.org/geo/1.0/"

val IMAGE_SUFFIX = "@2x.png"

enum class Units {
    standard, metric, imperial
}