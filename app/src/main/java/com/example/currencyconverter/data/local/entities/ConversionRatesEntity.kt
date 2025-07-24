package com.example.currencyconverter.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConversionRatesEntity(
    @PrimaryKey val currencyName: String,
    val conversionRate: Double
)