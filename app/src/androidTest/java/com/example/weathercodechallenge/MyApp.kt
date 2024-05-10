package com.example.weathercodechallenge

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

/**
 *
 */
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