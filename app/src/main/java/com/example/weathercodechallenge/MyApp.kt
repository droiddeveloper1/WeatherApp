package com.example.weathercodechallenge

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom application object that supports Hilt dependency injection and provides an
 * application-wide context object for classes needing one.
 *
 * Entire app is written in pure Kotlin - with no Java - as Java is lacking null-safety, so any
 * interaction with Java classes will need to add ugly null-checking syntax as we can never be
 * sure just what data Java will pass.
 *
 */
@HiltAndroidApp
class MyApp: Application(){

    override fun onCreate() {
        super.onCreate()
        MyApp.appContext = applicationContext
    }

    companion object {
        lateinit  var appContext: Context
    }
}