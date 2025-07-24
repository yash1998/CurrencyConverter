package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.data.remote.model.LatestConversionModel
import com.example.currencyconverter.utils.ApiResponse
import com.example.currencyconverter.utils.CurrencyConversionMap
import com.example.currencyconverter.utils.CurrencyNamesMap
import kotlinx.coroutines.flow.Flow

interface IConversionRatesRepository {

    suspend fun getLatestConversionRates(): ApiResponse<LatestConversionModel?>

    suspend fun getCurrencyList(): ApiResponse<CurrencyNamesMap?>
}