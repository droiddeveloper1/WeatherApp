package com.example.weathercodechallenge.presentation


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weathercodechallenge.domain.CityFromGeoLocation
import com.example.weathercodechallenge.domain.WeatherFromCityCountryUseCase
import com.example.weathercodechallenge.domain.WeatherFromCityStateCountryUseCase
import com.example.weathercodechallenge.domain.WeatherFromCityUseCase
import com.example.weathercodechallenge.model.Clouds
import com.example.weathercodechallenge.model.Geo
import com.example.weathercodechallenge.model.Physics
import com.example.weathercodechallenge.model.Rain
import com.example.weathercodechallenge.model.Sys
import com.example.weathercodechallenge.model.Weather
import com.example.weathercodechallenge.model.WeatherData
import com.example.weathercodechallenge.model.Wind
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
/*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
*/
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension

/**
 * local unit test, which will execute on the development machine (host).
 */
//@ExtendWith(MockitoExtension::class)
class WeatherViewModelTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestCoroutineScope(testDispatcher)  // new scope to avoid IllegalStateException during tests from coroutines

    //region DECLARE MOCK and SPY OBJECTS
    @Mock
    lateinit var mockContext: Context


    //endregion

    //region Rules
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    //endregion

    //region LOCALS
    private lateinit var viewModel: WeatherViewModel
    private lateinit var getCityWeatherUseCase: WeatherFromCityUseCase
    private lateinit var getCityCountryWeatherUseCase: WeatherFromCityCountryUseCase
    private lateinit var getCityStateCountryWeatherUseCase: WeatherFromCityStateCountryUseCase
    private lateinit var getCityFromReverseGeoUseCase: CityFromGeoLocation

    private val mockWeather = WeatherData(clouds = Clouds(all = 50),
        main = Physics(humidity = 31, temp = 17.55, feels_like = 15.9, grnd_level = 2, pressure = 91,
            sea_level = 300, temp_max = 22.9, temp_min = 16.3),
        base = "Sunny", cod = 0, dt = 0, id =0, geo = Geo(1.0,1.0), name = "",
        rain = Rain(0.0), sys = Sys("",0,0,0,0), timezone = 0,
        visibility = 0, weather=listOf(Weather(description = "Sunny", icon = "04d", id=5, main = "Clear Skies")),
        wind = Wind(1,1.0,1.0)
    )
    //endregion

    //region PRE- & POST- processors
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockContext = mock(Context::class.java)

        getCityWeatherUseCase = mockk<WeatherFromCityUseCase>()
        getCityCountryWeatherUseCase = mockk<WeatherFromCityCountryUseCase>()
        getCityStateCountryWeatherUseCase = mockk<WeatherFromCityStateCountryUseCase>()
        getCityFromReverseGeoUseCase = mockk<CityFromGeoLocation>()

        // Initialize the ViewModel with mocked use cases
        viewModel = WeatherViewModel(getCityWeatherUseCase,
            getCityCountryWeatherUseCase,
            getCityStateCountryWeatherUseCase,
            getCityFromReverseGeoUseCase)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
    //endregion

    @Test
    fun getWeatherData() {
    }

    @Test
    fun getLocationData() {
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchWeatherByCity() = testScope.runBlockingTest {
        val mockCity = "Big Town"
        coEvery { getCityWeatherUseCase(mockCity) } returns mockWeather

        viewModel.fetchWeatherByCity(mockCity)

        val job = launch {
            viewModel.weatherData.collect { relevantData ->
                assertNotNull(relevantData)
                assertEquals(31, relevantData?.humidity)
                assertEquals("Sunny", relevantData?.description)
                assertEquals(17.55, relevantData?.temperature)
            }
        }

        advanceUntilIdle()
        job.cancel() // Cancel the job after advancing time and making assertions
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchWeatherByCity sets weatherData to null on failure`() = testScope.runBlockingTest {
        val mockCity = "Metropolis"
        coEvery { getCityWeatherUseCase(mockCity) } returns null

        val job = launch {
            viewModel.weatherData.collect { data ->
                assertNull(data)
            }
        }

        viewModel.fetchWeatherByCity(mockCity)

        advanceUntilIdle()
        job.cancel() // Cancel the job after advancing time and making assertions
    }

    /*
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchWeatherByCity sets weatherData to null on failure`() = testScope.runBlockingTest {
        val mockCity = "Metropolis"
        coEvery { getCityWeatherUseCase(mockCity) } returns null

        viewModel.fetchWeatherByCity(mockCity)

        //testScheduler.apply { advanceTimeBy(2000); runCurrent() }

        viewModel.weatherData.collect { data ->
            assertNull(data)
        }
    }*/

    @Test
    fun fetchWeatherByCityCountry() {
    }

    @Test
    fun fetchWeatherByCityStateCountry() {
    }

    @Test
    fun fetchCityByReverseGeoLookup() {
    }
}
