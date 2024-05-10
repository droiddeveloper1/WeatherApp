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
 * For now, this class has been made 'open' in order to allow instrumented tests to run
 * using my custom test runner.
 * ToDo: explore a workaround that does not require the use of the 'open' prefix
 *
 */
@HiltAndroidApp
open class MyApp: Application(){

    override fun onCreate() {
        super.onCreate()
        MyApp.appContext = applicationContext
    }

    fun customMethod(): String = "Custom behavior"

    companion object {
        lateinit var appContext: Context
    }
}