package com.example.weathercodechallenge.model

data class CityFromReverseGeo(
    val data: List<CityFromReverseGeoItem>
)

data class CityFromReverseGeoItem(
    val country: String,
    val lat: Double,
    val lon: Double,
    val name: String
)