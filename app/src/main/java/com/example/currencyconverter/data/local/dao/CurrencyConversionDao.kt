package com.example.currencyconverter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconverter.data.local.entities.ConversionRatesEntity

@Dao
interface CurrencyConversionDao {

    @Query("SELECT * FROM ConversionRatesEntity")
    suspend fun getConversionRatesMap(): List<ConversionRatesEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertConversionRates(conversionRate: List<ConversionRatesEntity>)

}