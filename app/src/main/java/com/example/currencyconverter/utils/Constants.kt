package com.example.currencyconverter.utils

const val BASE_URL = "https://openexchangerates.org/api/"

const val API_KEY = "482d1c195aca4849b5d644621907bada"

const val POLL_DELAY = 1 * 60 * 1000L   // 30 minutes

const val FALLBACK_BASE_CURRENCY = "USD"


typealias CurrencyNamesMap = Map<String, String>

typealias CurrencyConversionMap = Map<String, Double>