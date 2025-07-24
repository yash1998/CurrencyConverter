package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.data.remote.model.LatestConversionModel

interface ILocalConversionRatesRepository : IConversionRatesRepository {

    suspend fun insertConversionRates(model: LatestConversionModel)

    suspend fun getLastUpdatedTimestamp(): Long
}