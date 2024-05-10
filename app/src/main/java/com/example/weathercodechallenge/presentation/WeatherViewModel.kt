package com.example.weathercodechallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathercodechallenge.common.INVALID_GEO
import com.example.weathercodechallenge.domain.CityFromGeoLocation
import com.example.weathercodechallenge.domain.WeatherFromCityCountryUseCase
import com.example.weathercodechallenge.domain.WeatherFromCityStateCountryUseCase
import com.example.weathercodechallenge.domain.WeatherFromCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * viewmodel for fetching and emitting weather data
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCityWeatherUseCase: WeatherFromCityUseCase,
    private val getCityCountryWeatherUseCase: WeatherFromCityCountryUseCase,
    private val getCityStateCountryWeatherUseCase: WeatherFromCityStateCountryUseCase,
    private val getCityFromReverseGeoUseCase: CityFromGeoLocation
): ViewModel()  {

    private val _weatherData = MutableStateFlow<RelevantWeatherData?>(null)
    val weatherData = _weatherData.asStateFlow()

    private val _locationData = MutableStateFlow<String>("")
    val locationData = _locationData.asStateFlow()

    fun fetchWeatherByCity(city: String) {

        viewModelScope.launch {
            // fetch the data
            val result = getCityWeatherUseCase(city)
            result?.let{
                val main = it.weather[0].main
                val description = it.weather[0].description
                val temperature = it.main.temp
                val humidity = it.main.humidity
                val cloudCoverage = it.clouds.all
                val icon = it.weather[0].icon

                val newData = RelevantWeatherData(main, description, temperature, humidity, cloudCoverage, icon)
                _weatherData.value = newData
            } ?: run {
                // send a datum that represents a failed fetch
                _weatherData.value = null
            }
        }

    }

    /**
     * UNUSED. TBD.
     */
    fun fetchWeatherByCityCountry(city: String, countryCode: String) {
        viewModelScope.launch {
            // fetch the data
            val result = getCityCountryWeatherUseCase(city, countryCode)

            // now extract just the useful pieces of data to display to user
            //
            // _weatherData.value = newData
        }
    }

    /**
     * UNUSED. TBD.
     */
    fun fetchWeatherByCityStateCountry(city: String, state: String, countryCode: String) {
        viewModelScope.launch {
            // fetch the data
            val result = getCityStateCountryWeatherUseCase(city, state, countryCode)

            // now extract just the useful pieces of data to display to user
            //
            // _weatherData.value = newData
        }
    }

    fun fetchCityByReverseGeoLookup(lat: Double, lon: Double){
        // only proceed with geo reverse lookup IFF valid locations present
        if(lat != INVALID_GEO.first && lon != INVALID_GEO.second) {
            viewModelScope.launch {
                // fetch the data
                val result = getCityFromReverseGeoUseCase(lat.toString(), lon.toString(), 1)
                result?.let {
                    val city: String = it[0].name

                    _locationData.value = city
                } ?: run {
                    _locationData.value = ""
                }
            }
        }
    }

}