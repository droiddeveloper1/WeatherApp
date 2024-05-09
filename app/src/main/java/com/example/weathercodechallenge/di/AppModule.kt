package com.example.weathercodechallenge.di

import android.app.Application
import android.content.Context
import com.example.weathercodechallenge.MyApp
import com.example.weathercodechallenge.common.PrefsManager
import com.example.weathercodechallenge.presentation.PREFS_KEY_CITY
import com.example.weathercodechallenge.repository.BASE_DATA_URL
import com.example.weathercodechallenge.repository.INetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideINetworkApi(): INetworkApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_DATA_URL)
            .build()
            .create(INetworkApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMyApp(app: Application): MyApp{
        return app as MyApp
    }

    @Provides
    @Singleton
    @Named("hello2")
    fun provideString2() = "Hello 2"
}