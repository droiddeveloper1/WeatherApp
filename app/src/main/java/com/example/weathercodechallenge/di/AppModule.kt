package com.example.weathercodechallenge.di

import android.app.Application
import com.example.weathercodechallenge.MyApp
import com.example.weathercodechallenge.repository.BASE_DATA_URL
import com.example.weathercodechallenge.repository.HTTPLogger
import com.example.weathercodechallenge.repository.INetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
            .client(HTTPLogger.getLogger())
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
    @Named("HelloTest")
    fun provideString2() = "Hello, Testing."
}