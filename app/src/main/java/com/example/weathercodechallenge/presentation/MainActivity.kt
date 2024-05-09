package com.example.weathercodechallenge.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.weathercodechallenge.BuildConfig
import com.example.weathercodechallenge.MyApp
import com.example.weathercodechallenge.repository.BASE_IMAGE_URL
import com.example.weathercodechallenge.repository.IMAGE_SUFFIX
import com.example.weathercodechallenge.repository.SecureStorage
import com.example.weathercodechallenge.ui.theme.WeatherCodeChallengeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Single-Activity app. Entry point for Hilt dependency injection
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val sharedPrefs = MyApp.appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val prefsEditor = sharedPrefs.edit()

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Store the API key from BuildConfig to EncryptedSharedPreferences
        SecureStorage.storeApiKey(this, BuildConfig.API_KEY)

        val vm by viewModels<WeatherViewModel>()

        enableEdgeToEdge()
        setContent {

            val locationPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            WeatherCodeChallengeTheme {

                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
                        Toast.makeText(this, "isGranted = $isGranted", Toast.LENGTH_SHORT).show()
                }
                if(!areLocationPermissionsAlreadyGranted()){
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
                                vm.data,
                                // pass just the relevant function from the viewmodel instead of passing entire viewmodel itself:
                                fetchWeatherData = vm::fetchWeatherByCity,
                                // pass a function arg instead of objects in order to enforce encapsulation and hide implementation logic away from Call site:
                                savePrefs = { key,data -> savePref(key,data) },
                                // pass a function arg instead of objects in order to enforce encapsulation and hide implementation logic away from Call site:
                                readPrefs = { key -> sharedPrefs.getString(key,"") ?: ""},

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
}

@Composable
fun MainScreen(stateflow: StateFlow<RelevantWeatherData?>,
               fetchWeatherData: (String) -> Unit,
               savePrefs: (String, String) -> Unit,
               readPrefs: (String) -> String,
               snackbarHostState: SnackbarHostState) {
    val data: State<RelevantWeatherData?> = stateflow.collectAsStateWithLifecycle()  // ensures that flow stops if composable removed
    val coroutineScope = rememberCoroutineScope()   // ensures that scope is cancelled if composable removed
    var oldText = remember { mutableStateOf(readPrefs(PREFS_KEY_CITY)) }
    val icon = data.value?.icon
    val imageUrl = BASE_IMAGE_URL + icon + IMAGE_SUFFIX

    Column(modifier = Modifier.padding(start = 35.dp, top = 100.dp)){
        TextField(
            value = oldText.value,
            onValueChange = { oldText.value = it },
            label = { Text("<enter city name>")},  // Label is displayed when the TextField is empty
            modifier = Modifier.padding(bottom = 30.dp)
            )

        Text(
            text = "Outlook : ${data.value?.main}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Temperature : ${data.value?.temperature} °C",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Humidity : ${data.value?.humidity} %",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Cloud Coverage : ${data.value?.cloudCoverage} %",
            modifier = Modifier.fillMaxWidth()
        )
        AsyncImage(
            model = imageUrl,
            //painter = painter,
            contentDescription = "weather image",
            modifier = Modifier.size(70.dp),
           // contentScale = ContentScale.Crop // Adjust cropping to suit your layout needs
        )

        Button(onClick = {
            coroutineScope.launch {
                // cache the current city value
                savePrefs(PREFS_KEY_CITY, oldText.value)

                // fetch new weather data from network
                fetchWeatherData(oldText.value)

                if(data.value == null){
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
        ), {}, ::dummy1, ::dummy2, SnackbarHostState())
    }
}

fun dummy1(a:String, b:String){}

fun dummy2(a:String):String{ return ""}

@Composable
fun DummyPreview() {
    val names = listOf("abc","def","ghi","JKLM")
    for (name in names) {
        Text("Hello $name", modifier = Modifier.background(Color.Red))
    }
}