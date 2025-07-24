package com.example.currencyconverter.utils

import android.content.Context
import android.widget.Toast
import kotlin.math.pow
import kotlin.math.roundToInt


fun Double.roundTo(digits: Int): Double {
    val power10 = 10.0.pow(digits)
    return (this * power10).roundToInt() / power10
}

fun String?.convert(baseMultiplier: Double, multiplier: Double): String {
    if (this == null || this.isEmpty()) return ""
    return (this.toLong() * multiplier / baseMultiplier).roundTo(2).toString()
}

fun String.addCommas(): String {
    if (this.isEmpty()) return this
    return String.format("%,d", this.toLong())
}

fun String.removeCommas(): String {
    if (this.isEmpty()) return this
    return replace(",", "")
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}