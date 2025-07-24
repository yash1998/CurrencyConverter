package com.example.currencyconverter.di

import com.example.currencyconverter.data.repository.LocalRepo
import com.example.currencyconverter.data.repository.RemoteRepo
import com.example.currencyconverter.di.qualifiers.LocalDataSource
import com.example.currencyconverter.di.qualifiers.RemoteDataSource
import com.example.currencyconverter.domain.repository.IConversionRatesRepository
import com.example.currencyconverter.domain.repository.ILocalConversionRatesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @LocalDataSource
    abstract fun getLocalConversionRepository(repository: LocalRepo): ILocalConversionRatesRepository

    @Binds
    @RemoteDataSource
    abstract fun getRemoteConversionRepository1(repository: RemoteRepo): IConversionRatesRepository

}