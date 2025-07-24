package com.example.currencyconverter.data.local

import com.example.currencyconverter.data.local.entities.ConversionRatesEntity
import com.example.currencyconverter.utils.CurrencyConversionMap


fun CurrencyConversionMap.toConversionRates(): List<ConversionRatesEntity> =
    this.map { (k, v) -> ConversionRatesEntity(k, v) }

fun List<ConversionRatesEntity>.toConversionMap(): CurrencyConversionMap =
    this.associate { it.currencyName to it.conversionRate }