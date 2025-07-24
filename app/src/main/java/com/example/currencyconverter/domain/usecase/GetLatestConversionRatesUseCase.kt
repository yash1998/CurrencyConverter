package com.example.currencyconverter.domain.usecase

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.currencyconverter.data.remote.model.LatestConversionModel
import com.example.currencyconverter.di.qualifiers.LocalDataSource
import com.example.currencyconverter.di.qualifiers.RemoteDataSource
import com.example.currencyconverter.domain.repository.IConversionRatesRepository
import com.example.currencyconverter.domain.repository.ILocalConversionRatesRepository
import com.example.currencyconverter.utils.ApiResponse
import com.example.currencyconverter.utils.POLL_DELAY
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLatestConversionRatesUseCase @Inject constructor(
    @LocalDataSource private val localRepo: ILocalConversionRatesRepository,
    @RemoteDataSource private val remoteRepo: IConversionRatesRepository,
    private val datasource: DataStore<Preferences>
) {
    suspend operator fun invoke() = flow {
        emit(ApiResponse.InProgress)
        if (isCacheExpired(localRepo.getLastUpdatedTimestamp()).not()) {
            val local = localRepo.getLatestConversionRates()         // 1. get offline data
            if (local is ApiResponse.Failure) { // 2. check offline data is present/not
                emit(callRemote())  // 5. return remote data
            } else {
                emit(local)       // 6. return local data
            }
        } else {
            emit(callRemote())
        }
    }

    private suspend fun callRemote(): ApiResponse<LatestConversionModel?> {
        val remote = remoteRepo.getLatestConversionRates()       // 3. if not, get data from remote
        if (remote is ApiResponse.Failure) {
            return localRepo.getLatestConversionRates()
        }
        if (remote is ApiResponse.Success && remote.data != null) {
            localRepo.insertConversionRates(remote.data)   // 4. insert into db
        }
        return remote
    }

    private fun isCacheExpired(lastUpdatedTs: Long): Boolean {
        return lastUpdatedTs == -1L || System.currentTimeMillis() - lastUpdatedTs >= POLL_DELAY
    }
}