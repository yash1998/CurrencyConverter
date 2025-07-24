package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.remote.CurrencyConverterApiService
import com.example.currencyconverter.data.remote.model.LatestConversionModel
import com.example.currencyconverter.domain.repository.IConversionRatesRepository
import com.example.currencyconverter.utils.ApiFailure
import com.example.currencyconverter.utils.ApiResponse
import com.example.currencyconverter.utils.CurrencyNamesMap
import javax.inject.Inject

class RemoteRepo @Inject constructor(
    val apiService: CurrencyConverterApiService
) : IConversionRatesRepository {

    override suspend fun getLatestConversionRates(): ApiResponse<LatestConversionModel?> {
        try {
            val response = apiService.getLatestConversionRates()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                return ApiResponse.Success(body)
            } else {
                return ApiResponse.Failure(ApiFailure(response.message(), response.code()))
            }
        } catch (e: Exception) {
            return ApiResponse.Failure(ApiFailure(e.message.toString()))
        }
    }

    override suspend fun getCurrencyList(): ApiResponse<CurrencyNamesMap?> {
        try {
            val response = apiService.getCurrencyList()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                return ApiResponse.Success(body)
            } else {
                return ApiResponse.Failure(ApiFailure(response.message(), response.code()))
            }
        } catch (e: Exception) {
            return ApiResponse.Failure(ApiFailure(e.message.toString()))
        }
    }
}