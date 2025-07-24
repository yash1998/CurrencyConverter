package com.example.currencyconverter.utils

sealed interface UiState {
    data class Success<T>(val data: T) : UiState
    data class Error(val message: String) : UiState
    data object Loading : UiState
    data object Default : UiState
}