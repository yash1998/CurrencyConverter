package com.example.currencyconverter.data.remote

import com.example.currencyconverter.data.remote.model.LatestConversionModel
import com.example.currencyconverter.utils.API_KEY
import com.example.currencyconverter.utils.CurrencyNamesMap
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyConverterApiService {

    @GET("currencies.json")
    suspend fun getCurrencyList(): Response<CurrencyNamesMap>

    @GET("latest.json?app_id=$API_KEY")
    suspend fun getLatestConversionRates(): Response<LatestConversionModel>
}