package com.example.weathercodechallenge

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class CustomTestRunner  : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        // Use the generated application class for Hilt
        return super.newApplication(cl, HiltTestApplication_Application::class.java.name, context)
    }
}