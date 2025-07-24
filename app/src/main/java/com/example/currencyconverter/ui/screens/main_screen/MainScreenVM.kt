package com.example.currencyconverter.ui.screens.main_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.usecase.CurrencyListUseCase
import com.example.currencyconverter.domain.usecase.GetLatestConversionRatesUseCase
import com.example.currencyconverter.utils.ApiResponse
import com.example.currencyconverter.utils.CurrencyNamesMap
import com.example.currencyconverter.utils.POLL_DELAY
import com.example.currencyconverter.utils.UiState
import com.example.currencyconverter.utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenVM @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    @Inject
    lateinit var getLatestConversionRatesUseCase: GetLatestConversionRatesUseCase

    @Inject
    lateinit var getCurrencyListUseCase: CurrencyListUseCase

    private val _currencyNamesMap = MutableStateFlow<UiState>(UiState.Default)
    val currencyNamesMap: StateFlow<UiState> = _currencyNamesMap

    private val _currencyConversionMap = MutableStateFlow<UiState>(UiState.Default)
    val currencyConversionMap: StateFlow<UiState> = _currencyConversionMap

    fun getCurrencyList() {
        viewModelScope.launch {
            getCurrencyListUseCase.invoke().collectLatest { response ->
                _currencyNamesMap.value = when (response) {
                    is ApiResponse.InProgress -> UiState.Loading
                    is ApiResponse.Failure -> UiState.Error(response.apiFailure.message)
                    is ApiResponse.Success -> UiState.Success(response.data)
                }
            }
        }
    }

    fun pollLatestConversionRates() {
        viewModelScope.launch {
            getLatestConversionRatesUseCase.invoke().collectLatest { response ->
                when (response) {
                    is ApiResponse.InProgress -> {
                        if (_currencyConversionMap.value !is UiState.Success<*>) {
                            _currencyConversionMap.value = UiState.Loading
                        }
                    }
                    is ApiResponse.Failure -> {
                        if (_currencyConversionMap.value !is UiState.Success<*>) {
                            _currencyConversionMap.value = UiState.Error(response.apiFailure.message)
                        }
                    }
                    is ApiResponse.Success -> {
                        _currencyConversionMap.value = UiState.Success(response.data)
                    }
                }
            }
        }
    }
}