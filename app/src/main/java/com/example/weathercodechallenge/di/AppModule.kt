package com.example.weathercodechallenge.di

import android.content.Context
import com.example.weathercodechallenge.MyApp
import com.example.weathercodechallenge.common.PrefsManager
import com.example.weathercodechallenge.presentation.PREFS_KEY_CITY
import com.example.weathercodechallenge.repository.BASE_URL
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
            .baseUrl(BASE_URL)
            .build()
            .create(INetworkApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMyApp(@ApplicationContext app: Context): MyApp{
        return app as MyApp
    }

    /**
     * provides cached city name from a prior user session.
     * this is useful for unit-testing because this @Provides function can be
     * replaced with a dummy that simply injects a new/fake city
     */
    @Provides
    @Singleton
    @Named("city")
    fun provideCityName(@ApplicationContext ctx: Context) = runBlocking {
        val cachedCity = PrefsManager.readFromDatastore(PREFS_KEY_CITY, ctx) ?: ""
        return@runBlocking cachedCity
    }

    @Provides
    @Singleton
    @Named("hello2")
    fun provideString2() = "Hello 2"
}