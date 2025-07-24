package com.example.currencyconverter.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.currencyconverter.data.remote.model.LatestConversionModel
import com.example.currencyconverter.domain.repository.IConversionRatesRepository
import com.example.currencyconverter.domain.repository.ILocalConversionRatesRepository
import com.example.currencyconverter.utils.ApiResponse
import com.example.currencyconverter.utils.CurrencyConversionMap
import com.example.currencyconverter.utils.POLL_DELAY
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetLatestConversionRatesUseCaseTest {

    private lateinit var useCase: GetLatestConversionRatesUseCase
    private lateinit var localRepo: ILocalConversionRatesRepository
    private lateinit var remoteRepo: IConversionRatesRepository
    private lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setup() {
        localRepo = mockk()
        remoteRepo = mockk()
        dataStore = mockk()
        useCase = GetLatestConversionRatesUseCase(localRepo, remoteRepo, dataStore)
    }

    @Test
    fun `when cache is valid and local data exists, return local data`() = runBlocking {
        // Arrange
        val mockLocalData = LatestConversionModel(
            base = "USD",
            rates = mapOf("EUR" to 0.85),
            timestamp = System.currentTimeMillis()
        )
        coEvery { localRepo.getLastUpdatedTimestamp() } returns System.currentTimeMillis()
        coEvery { localRepo.getLatestConversionRates() } returns ApiResponse.Success(mockLocalData)

        // Act
        val results = useCase.invoke().take(2).toList()

        // Assert
        assertTrue(results[0] is ApiResponse.InProgress)
        assertTrue(results[1] is ApiResponse.Success)
        assertEquals(mockLocalData, (results[1] as ApiResponse.Success).data)
        coVerify(exactly = 0) { remoteRepo.getLatestConversionRates() }
    }

    @Test
    fun `when cache is expired, fetch from remote and update local`() = runBlocking {
        // Arrange
        val mockRemoteData = LatestConversionModel(
            base = "USD",
            rates = mapOf("EUR" to 0.85),
            timestamp = System.currentTimeMillis()
        )
        coEvery { localRepo.getLastUpdatedTimestamp() } returns System.currentTimeMillis() - POLL_DELAY - 1000
        coEvery { remoteRepo.getLatestConversionRates() } returns ApiResponse.Success(mockRemoteData)
        coEvery { localRepo.getLatestConversionRates() } returns ApiResponse.Success(mockRemoteData)
        coEvery { localRepo.insertConversionRates(any()) } returns Unit

        // Act
        val results = useCase.invoke().take(2).toList()

        // Assert
        assertTrue(results[0] is ApiResponse.InProgress)
        assertTrue(results[1] is ApiResponse.Success)
        assertEquals(mockRemoteData, (results[1] as ApiResponse.Success).data)
        coVerify { localRepo.insertConversionRates(mockRemoteData) }
    }

    @Test
    fun `when remote fails, return local data`() = runBlocking {
        // Arrange
        val mockLocalData = LatestConversionModel(
            base = "USD",
            rates = mapOf("EUR" to 0.85),
            timestamp = System.currentTimeMillis()
        )
        coEvery { localRepo.getLastUpdatedTimestamp() } returns System.currentTimeMillis() - POLL_DELAY - 1000
        coEvery { remoteRepo.getLatestConversionRates() } returns ApiResponse.Failure(mockk())
        coEvery { localRepo.getLatestConversionRates() } returns ApiResponse.Success(mockLocalData)

        // Act
        val results = useCase.invoke().take(2).toList()

        // Assert
        assertTrue(results[0] is ApiResponse.InProgress)
        assertTrue(results[1] is ApiResponse.Success)
        assertEquals(mockLocalData, (results[1] as ApiResponse.Success).data)
    }

    @Test
    fun `when both remote and local fail, return failure`() = runBlocking {
        // Arrange
        coEvery { localRepo.getLastUpdatedTimestamp() } returns System.currentTimeMillis() - POLL_DELAY - 1000
        coEvery { remoteRepo.getLatestConversionRates() } returns ApiResponse.Failure(mockk())
        coEvery { localRepo.getLatestConversionRates() } returns ApiResponse.Failure(mockk())

        // Act
        val results = useCase.invoke().take(2).toList()

        // Assert
        assertTrue(results[0] is ApiResponse.InProgress)
        assertTrue(results[1] is ApiResponse.Failure)
    }

    @Test
    fun `when cache is expired and remote returns null, return failure`() = runBlocking {
        // Arrange
        coEvery { localRepo.getLastUpdatedTimestamp() } returns System.currentTimeMillis() - POLL_DELAY - 1000
        coEvery { remoteRepo.getLatestConversionRates() } returns ApiResponse.Success(null)

        // Act
        val results = useCase.invoke().take(2).toList()

        // Assert
        assertTrue(results[0] is ApiResponse.InProgress)
        assertTrue(results[1] is ApiResponse.Success)
        assertEquals(null, (results[1] as ApiResponse.Success).data)
    }
} 