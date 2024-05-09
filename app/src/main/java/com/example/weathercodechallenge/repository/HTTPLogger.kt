package com.example.weathercodechallenge.repository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object HTTPLogger {
    fun getLogger(): OkHttpClient {
        /*
         * OKHTTP interceptor - logs all API calls
         */
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        return client
    }
}