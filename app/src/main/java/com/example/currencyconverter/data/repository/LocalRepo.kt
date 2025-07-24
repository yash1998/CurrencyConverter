package com.example.currencyconverter.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.currencyconverter.data.local.dao.CurrencyConversionDao
import com.example.currencyconverter.data.local.toConversionMap
import com.example.currencyconverter.data.local.toConversionRates
import com.example.currencyconverter.data.prefs.DataStoreKeys
import com.example.currencyconverter.data.remote.model.LatestConversionModel
import com.example.currencyconverter.domain.repository.ILocalConversionRatesRepository
import com.example.currencyconverter.utils.ApiFailure
import com.example.currencyconverter.utils.ApiResponse
import com.example.currencyconverter.utils.CurrencyNamesMap
import com.example.currencyconverter.utils.FALLBACK_BASE_CURRENCY
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocalRepo @Inject constructor(
    private val conversionRatesDao: CurrencyConversionDao,
    private val datastore: DataStore<Preferences>
) : ILocalConversionRatesRepository {

    override suspend fun getLatestConversionRates(): ApiResponse<LatestConversionModel?> {
        val conversionRatesMap = conversionRatesDao.getConversionRatesMap()
        if (conversionRatesMap.isEmpty()) {
            return ApiResponse.Failure(ApiFailure())
        } else {
            return ApiResponse.Success(
                LatestConversionModel(
                    rates = conversionRatesMap.toConversionMap(),
                    base = datastore.data.first()[DataStoreKeys.LAST_UPDATED_BASE_CURRENCY]
                        ?: FALLBACK_BASE_CURRENCY,
                    timestamp = datastore.data.first()[DataStoreKeys.LAST_UPDATED_TIMESTAMP] ?: -1L
                )
            )
        }
    }

    override suspend fun getCurrencyList(): ApiResponse<CurrencyNamesMap?> {
        return ApiResponse.Failure(ApiFailure())
    }

    override suspend fun getLastUpdatedTimestamp(): Long {
        return datastore.data.first()[DataStoreKeys.LAST_UPDATED_TIMESTAMP] ?: -1L
    }

    override suspend fun insertConversionRates(model: LatestConversionModel) {
        conversionRatesDao.insertConversionRates(model.rates.toConversionRates())
        datastore.edit { prefs ->
            prefs[DataStoreKeys.LAST_UPDATED_TIMESTAMP] = System.currentTimeMillis()
            prefs[DataStoreKeys.LAST_UPDATED_BASE_CURRENCY] = model.base
        }
    }
}