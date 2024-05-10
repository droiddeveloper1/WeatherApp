package com.example.weathercodechallenge.di

import android.app.Application
import android.content.Context
import com.example.weathercodechallenge.MyApp
import com.example.weathercodechallenge.repository.BASE_DATA_URL
import com.example.weathercodechallenge.repository.BASE_REVERSE_GEO_URL
import com.example.weathercodechallenge.repository.HTTPLogger
import com.example.weathercodechallenge.repository.INetworkApi
import com.example.weathercodechallenge.repository.INetworkApi2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
class TestAppModule {

    @Provides
    @Singleton
    @Named("HelloTest")
    fun provideString2() = "Hello, Testing!"

    @Provides
    @Singleton
    @Named("AnotherStringTest")
    fun provideDummyData2() = "additional string test!"


    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideMyApp(app: Application): MyApp{
        return app as MyApp
    }

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

    @Provides
    @Singleton
    fun provideINetworkApi2(): INetworkApi2 {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_REVERSE_GEO_URL)
            .client(HTTPLogger.getLogger())
            .build()
            .create(INetworkApi2::class.java)
    }
}