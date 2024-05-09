package com.example.weathercodechallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathercodechallenge.domain.WeatherFromCityCountryUseCase
import com.example.weathercodechallenge.domain.WeatherFromCityStateCountryUseCase
import com.example.weathercodechallenge.domain.WeatherFromCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * view model for fetching and emitting weather data
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCityWeatherUseCase: WeatherFromCityUseCase,
    private val getCityCountryWeatherUseCase: WeatherFromCityCountryUseCase,
    private val getCityStateCountryWeatherUseCase: WeatherFromCityStateCountryUseCase
): ViewModel()  {

    private val _data = MutableStateFlow<RelevantWeatherData?>(null)
    val data = _data.asStateFlow()

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
                _data.value = newData
            } ?: run {
                // send a datum that represents a failed fetch
                _data.value = null
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
            // _data.value = newData
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
            // _data.value = newData
        }
    }

}