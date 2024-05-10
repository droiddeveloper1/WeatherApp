package com.example.weathercodechallenge.presentation

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.weathercodechallenge.BuildConfig
import com.example.weathercodechallenge.MyApp
import com.example.weathercodechallenge.common.LocationManager
import com.example.weathercodechallenge.repository.BASE_IMAGE_URL
import com.example.weathercodechallenge.repository.IMAGE_SUFFIX
import com.example.weathercodechallenge.repository.SecureStorage
import com.example.weathercodechallenge.ui.theme.WeatherCodeChallengeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Single-Activity app. Entry point for Hilt dependency injection
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"
    lateinit var sharedPrefs: SharedPreferences
    lateinit var prefsEditor: SharedPreferences.Editor
    var isLocationAlreadyFetched = true

    private val vm by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefsEditor = sharedPrefs.edit()

        // Store the API key from BuildConfig to EncryptedSharedPreferences
        SecureStorage.storeApiKey(this, BuildConfig.API_KEY)

        enableEdgeToEdge()
        setContent {

            val locationPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            WeatherCodeChallengeTheme {

                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()) {
                       // Toast.makeText(this, "isGranted = $it", Toast.LENGTH_SHORT).show()
                }
                if(!areLocationPermissionsAlreadyGranted()){
                    isLocationAlreadyFetched = false
                    LaunchedEffect(Unit) {
                        launcher.launch(locationPermissions)
                    }
                }

                // State to manage when the Snackbar is shown
                val snackbarVisibleState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarVisibleState)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.CenterVertically))
                    { innerPadding ->

                    //Dummy(listOf("abc","def","ghi","JKLM"))

                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable(route = "main_screen") {
                            MainScreen(
                                // to enforce OO encapsulation, do not pass viewmodel directly to any composable; pass just the state instead:
                                vm.weatherData,
                                // to enforce OO encapsulation, do not pass viewmodel directly to any composable; pass just the state instead:
                                vm.locationData,
                                // pass just the relevant function from the viewmodel instead of passing entire viewmodel itself:
                                fetchWeatherData = vm::fetchWeatherByCity,
                                // pass a function arg instead of objects in order to enforce encapsulation and hide implementation logic away from Call site:
                                savePrefs = { key,data -> savePref(key,data) },
                                // pass a function arg instead of objects in order to enforce encapsulation and hide implementation logic away from Call site:
                                readPrefs = { key -> sharedPrefs.getString(key,"") ?: ""},
                                // pass a function arg instead of objects in order to enforce encapsulation and hide implementation logic away from Call site:
                                resetLocationFlag = { -> resetLocationFetchFlag() },

                                isLocationFlagSet = isLocationAlreadyFetched,

                                snackbarHostState = snackbarVisibleState
                            )
                        }
                        composable(route = "screen_two") {
                            // @ToDo additional screens to be implemented as needed ...
                            // ScreenTwo()
                        }
                        composable(route = "screen_three") {
                            // @ToDo additional screens to be implemented as needed ...
                            // ScreenThree()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // listen for GPS data on an IO thread since it is blocking, as opposed to CPU-intensive
        lifecycleScope.launch(Dispatchers.IO){
            val lm = LocationManager()
            lm.getLocation(this@MainActivity)
            // capture the next geo data emitted from the flow:
            lm.locationFlow.collect{
                it?.let{ geoData ->
                    // initiate city name lookup
                    vm.fetchCityByReverseGeoLookup(geoData.first, geoData.second)
                    Log.d(TAG,"Latitude: ${geoData.first}, Longitude: ${geoData.second}")

                }
            }
        }
    }

    private fun areLocationPermissionsAlreadyGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun savePref(key:String, value:String){
        prefsEditor.apply {
            putString(key, value)
            apply()
        }
    }

    fun resetLocationFetchFlag()
    {
        isLocationAlreadyFetched = true
    }
}

@Composable
fun MainScreen(weatherStateflow: StateFlow<RelevantWeatherData?>,
               locationStateflow: StateFlow<String>,
               fetchWeatherData: (String) -> Unit,
               savePrefs: (String, String) -> Unit,
               readPrefs: (String) -> String,
               resetLocationFlag: () -> Unit,
               isLocationFlagSet: Boolean,
               snackbarHostState: SnackbarHostState) {
    val weatherData: State<RelevantWeatherData?> = weatherStateflow.collectAsStateWithLifecycle()  // ensures that flow stops if composable removed
    val locationData: State<String> = locationStateflow.collectAsStateWithLifecycle()     // ensures that flow stops if composable removed
    val coroutineScope = rememberCoroutineScope()   // ensures that scope is cancelled if composable removed
    var oldText = remember { mutableStateOf(readPrefs(PREFS_KEY_CITY)) }
    val icon = weatherData.value?.icon
    val imageUrl = BASE_IMAGE_URL + icon + IMAGE_SUFFIX

    /**
     * this block runs only when new GPS location data is fetched
     * It prefetches the user's local weather using the reverse geo lookup request
     */
    LaunchedEffect(locationData.value) {
       // if(!isLocationFlagSet){
        resetLocationFlag()

        val city = locationData.value
        if(city.isNotBlank()) {
            // now fetch new weather data from network using the detected local city name
            fetchWeatherData(city)
            oldText.value = city
        }
      //  }
    }

    Column(modifier = Modifier.padding(start = 35.dp, top = 100.dp)){
        TextField(
            value = oldText.value,
            onValueChange = { oldText.value = it },
            label = { Text("<Enter city name>")},  // Label is displayed when the TextField is empty
            modifier = Modifier.padding(bottom = 30.dp)
            )

        Text(
            text = "Outlook : ${weatherData.value?.main}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Temperature : ${weatherData.value?.temperature} °C",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Humidity : ${weatherData.value?.humidity} %",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Cloud Coverage : ${weatherData.value?.cloudCoverage} %",
            modifier = Modifier.fillMaxWidth()
        )
        AsyncImage(
            model = imageUrl.trim(),
            //painter = painter,
            contentDescription = "weather image",
            modifier = Modifier.size(100.dp),
           // contentScale = ContentScale.Crop // Adjust cropping to suit your layout needs
        )

        Button(onClick = {
            coroutineScope.launch {
                // cache the current city value
                savePrefs(PREFS_KEY_CITY, oldText.value)

                // fetch new weather data from network
                fetchWeatherData(oldText.value)

                if(weatherData.value == null){
                    snackbarHostState.showSnackbar(
                        message = "No Data Found for this city",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp)) {
            Text("Update Weather Data")
        }
    }
}

@Composable
fun Dummy(names: List<String>) {
    for (name in names) {
        Text("Hello $name", modifier = Modifier.background(Color.Red))
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    WeatherCodeChallengeTheme {
        MainScreen(MutableStateFlow<RelevantWeatherData>(
            RelevantWeatherData(
            "abd",
            "def",
            34.79,
            29,
            77,
                "10d")
        ),
            MutableStateFlow("RandomCity"),
            {}, ::dummy1, ::dummy2, ::dummy3, false, SnackbarHostState())
    }
}

fun dummy1(a:String, b:String){}

fun dummy2(a:String):String{ return ""}

fun dummy3():Unit {}

@Composable
fun DummyPreview() {
    val names = listOf("abc","def","ghi","JKLM")
    for (name in names) {
        Text("Hello $name", modifier = Modifier.background(Color.Red))
    }
}