package com.example.weathercodechallenge.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weathercodechallenge.BuildConfig
import com.example.weathercodechallenge.common.PrefsManager
import com.example.weathercodechallenge.repository.SecureStorage
import com.example.weathercodechallenge.ui.theme.WeatherCodeChallengeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    @Named("city")
    lateinit var cachedCity:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Store the API key from BuildConfig to EncryptedSharedPreferences
        SecureStorage.storeApiKey(this, BuildConfig.API_KEY)

        val vm by viewModels<WeatherViewModel>()

        enableEdgeToEdge()
        setContent {
            WeatherCodeChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        vm.data,   // to enforce OO encapsulation, do not pass viewmodel directly to any composable; pass just the state instead
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(stateflow: StateFlow<RelevantWeatherData?>, modifier: Modifier = Modifier) {
    val data = stateflow.collectAsStateWithLifecycle()  // ensures that flow stops if composable removed
    val coroutineScope = rememberCoroutineScope()   // ensures that scope is cancelled if composable removed
    var oldText = remember {mutableStateOf("")}
    val ctx = LocalContext.current
    
    Column(modifier = Modifier.padding(start = 15.dp)){
        TextField(
            value = oldText.value,
            onValueChange = { oldText.value = it },
            label = { Text("Enter city name")},  // Label is displayed when the TextField is empty
            modifier = Modifier.padding(bottom = 15.dp)
            )

        Text(
            text = "Outlook : ${data.value?.main}",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Temperature : ${data.value?.temperature}",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Humidity : ${data.value?.humidity}",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Cloud Coverage : ${data.value?.cloudCoverage}",
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            coroutineScope.launch {
                // cache the current city value
                PrefsManager.saveToDatastore(PREFS_KEY_CITY, oldText.value, ctx)


                // Perform some coroutine work here, like fetching data from a repository
                println("Doing some asynchronous work")
            }
        },
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(top = 30.dp)) {
            Text("Update Weather Data")
        }
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
            77)
        ))
    }
}
