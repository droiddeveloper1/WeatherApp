package com.example.weathercodechallenge.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationManager {

    private val TAG = "LocationUtils"
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val _locationFlow = MutableStateFlow<Pair<Double, Double>?>(INVALID_GEO) // initialize to invalid Lat/Long
    val locationFlow = _locationFlow.asStateFlow()

    fun getLocation(ctx: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)

        val locationRequest = LocationRequest.Builder(10000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdates(1)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    // Use the location object
                    val latitude = location.latitude
                    val longitude = location.longitude

                    Log.d(TAG,"Latitude: $latitude, Longitude: $longitude")

                    _locationFlow.value = Pair(latitude,longitude)

                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Handle case of ungranted permissions
            //
            //

            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            ctx.mainLooper
        )
    }
}