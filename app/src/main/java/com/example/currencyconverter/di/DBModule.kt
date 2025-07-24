package com.example.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.example.currencyconverter.R
import com.example.currencyconverter.data.local.dao.CurrencyConversionDao
import com.example.currencyconverter.data.local.database.CurrencyDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DBModule {

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context): CurrencyDB {
        return Room.databaseBuilder(
            context, CurrencyDB::class.java, context.getString(R.string.currencydb_name)
        ).build()
    }

    @Singleton
    @Provides
    fun provideConversionRatesDao(room: CurrencyDB): CurrencyConversionDao {
        return room.conversionRatesDao()
    }
}