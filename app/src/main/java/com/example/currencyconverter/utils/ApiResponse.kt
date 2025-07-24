package com.example.currencyconverter.utils

sealed class ApiResponse<out T>(data: T? = null, apiFailure: ApiFailure? = null) {
    data class Success<T>(val data: T) : ApiResponse<T>(data = data)
    data object InProgress : ApiResponse<Nothing>()
    data class Failure(val apiFailure: ApiFailure) : ApiResponse<Nothing>(apiFailure = apiFailure)
}