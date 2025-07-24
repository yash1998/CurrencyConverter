package com.example.currencyconverter.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.example.currencyconverter.utils.CurrencyConversionMap

@Keep
@Serializable
data class LatestConversionModel(
    @SerialName("base")
    val base: String,
    @SerialName("disclaimer")
    val disclaimer: String? = null,
    @SerialName("license")
    val license: String? = null,
    @SerialName("rates")
    val rates: CurrencyConversionMap,
    @SerialName("timestamp")
    val timestamp: Long
)