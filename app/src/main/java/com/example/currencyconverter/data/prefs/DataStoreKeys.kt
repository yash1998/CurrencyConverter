package com.example.currencyconverter.data.prefs

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {

    val LAST_UPDATED_TIMESTAMP = longPreferencesKey("last_updated_timestamp")
    val LAST_UPDATED_BASE_CURRENCY = stringPreferencesKey("last_updated_base_currency")
}