package com.example.currencyconverter.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyconverter.data.local.dao.CurrencyConversionDao
import com.example.currencyconverter.data.local.entities.ConversionRatesEntity

@Database(
    entities = [ConversionRatesEntity::class],
    version = 1
)
abstract class CurrencyDB : RoomDatabase() {
    abstract fun conversionRatesDao(): CurrencyConversionDao
}