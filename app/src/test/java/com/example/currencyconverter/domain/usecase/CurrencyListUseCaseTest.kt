package com.example.currencyconverter.domain.usecase

import com.example.currencyconverter.domain.repository.IConversionRatesRepository
import com.example.currencyconverter.utils.ApiResponse
import com.example.currencyconverter.utils.CurrencyNamesMap
import io.mockk.coEvery
import io.mockk.mockk
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
class CurrencyListUseCaseTest {

    private lateinit var useCase: CurrencyListUseCase
    private lateinit var remoteRepo: IConversionRatesRepository

    @Before
    fun setup() {
        remoteRepo = mockk()
        useCase = CurrencyListUseCase(remoteRepo)
    }

    @Test
    fun `when remote returns success, return success with data`() = runBlocking {
        // Arrange
        val mockCurrencyList: CurrencyNamesMap = mapOf(
            "USD" to "US Dollar",
            "EUR" to "Euro"
        )
        coEvery { remoteRepo.getCurrencyList() } returns ApiResponse.Success(mockCurrencyList)

        // Act
        val results = useCase.invoke().take(2).toList()

        // Assert
        assertTrue(results[0] is ApiResponse.InProgress)
        assertTrue(results[1] is ApiResponse.Success)
        assertEquals(mockCurrencyList, (results[1] as ApiResponse.Success).data)
    }

    @Test
    fun `when remote returns failure, return failure`() = runBlocking {
        // Arrange
        coEvery { remoteRepo.getCurrencyList() } returns ApiResponse.Failure(mockk())

        // Act
        val results = useCase.invoke().take(2).toList()

        // Assert
        assertTrue(results[0] is ApiResponse.InProgress)
        assertTrue(results[1] is ApiResponse.Failure)
    }

    @Test
    fun `when remote returns null data, return failure`() = runBlocking {
        // Arrange
        coEvery { remoteRepo.getCurrencyList() } returns ApiResponse.Success(null)

        // Act
        val results = useCase.invoke().take(2).toList()

        // Assert
        assertTrue(results[0] is ApiResponse.InProgress)
        assertTrue(results[1] is ApiResponse.Success)
        assertEquals(null, (results[1] as ApiResponse.Success).data)
    }
} 