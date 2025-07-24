package com.example.currencyconverter.domain.usecase

import com.example.currencyconverter.di.qualifiers.RemoteDataSource
import com.example.currencyconverter.domain.repository.IConversionRatesRepository
import com.example.currencyconverter.utils.ApiResponse
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyListUseCase @Inject constructor(
    @RemoteDataSource private val remoteRepo: IConversionRatesRepository
) {
    suspend operator fun invoke() = flow {
        emit(ApiResponse.InProgress)
        emit(remoteRepo.getCurrencyList())
    }
}