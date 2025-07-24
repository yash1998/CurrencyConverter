package com.example.currencyconverter.di

import com.example.currencyconverter.data.remote.CurrencyConverterApiService
import com.example.currencyconverter.utils.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(jsonSerObj: Json, loggingInterceptor: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).client(loggingInterceptor)
            .addConverterFactory(jsonSerObj.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Singleton
    @Provides
    fun getWeatherForecastService(retrofit: Retrofit): CurrencyConverterApiService {
        return retrofit.create(CurrencyConverterApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }).build()
    }
}