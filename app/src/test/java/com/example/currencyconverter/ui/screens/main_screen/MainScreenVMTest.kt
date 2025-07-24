package com.example.currencyconverter.ui.screens.main_screen

import androidx.lifecycle.SavedStateHandle
import com.example.currencyconverter.data.remote.model.LatestConversionModel
import com.example.currencyconverter.domain.usecase.CurrencyListUseCase
import com.example.currencyconverter.domain.usecase.GetLatestConversionRatesUseCase
import com.example.currencyconverter.utils.ApiResponse
import com.example.currencyconverter.utils.CurrencyNamesMap
import com.example.currencyconverter.utils.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class MainScreenVMTest {

    private lateinit var viewModel: MainScreenVM
    private lateinit var getLatestConversionRatesUseCase: GetLatestConversionRatesUseCase
    private lateinit var getCurrencyListUseCase: CurrencyListUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getLatestConversionRatesUseCase = mockk()
        getCurrencyListUseCase = mockk()
        viewModel = MainScreenVM(SavedStateHandle())
        viewModel.getLatestConversionRatesUseCase = getLatestConversionRatesUseCase
        viewModel.getCurrencyListUseCase = getCurrencyListUseCase
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getCurrencyList returns success, update currencyNamesMap with success state`() = runTest {
        // Arrange
        val mockCurrencyList: CurrencyNamesMap = mapOf(
            "USD" to "US Dollar",
            "EUR" to "Euro"
        )
        coEvery { getCurrencyListUseCase.invoke() } returns flowOf(
            ApiResponse.InProgress,
            ApiResponse.Success(mockCurrencyList)
        )

        // Act
        viewModel.getCurrencyList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.currencyNamesMap.value
        assertTrue(state is UiState.Success<*>)
        assertEquals(mockCurrencyList, (state as UiState.Success<*>).data)
    }

    @Test
    fun `when getCurrencyList returns failure, update currencyNamesMap with error state`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        coEvery { getCurrencyListUseCase.invoke() } returns flowOf(
            ApiResponse.InProgress,
            ApiResponse.Failure(mockk { coEvery { message } returns errorMessage })
        )

        // Act
        viewModel.getCurrencyList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.currencyNamesMap.value
        assertTrue(state is UiState.Error)
        assertEquals(errorMessage, (state as UiState.Error).message)
    }

    @Test
    fun `when pollLatestConversionRates returns success, update currencyConversionMap with success state`() = runTest {
        // Arrange
        val mockConversionRates = LatestConversionModel(
            base = "USD",
            rates = mapOf("EUR" to 0.85),
            timestamp = System.currentTimeMillis()
        )
        coEvery { getLatestConversionRatesUseCase.invoke() } returns flowOf(
            ApiResponse.InProgress,
            ApiResponse.Success(mockConversionRates)
        )

        // Act
        viewModel.pollLatestConversionRates()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.currencyConversionMap.value
        assertTrue(state is UiState.Success<*>)
        assertEquals(mockConversionRates, (state as UiState.Success<*>).data)
    }

    @Test
    fun `when pollLatestConversionRates returns failure, update currencyConversionMap with error state`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        coEvery { getLatestConversionRatesUseCase.invoke() } returns flowOf(
            ApiResponse.InProgress,
            ApiResponse.Failure(mockk { coEvery { message } returns errorMessage })
        )

        // Act
        viewModel.pollLatestConversionRates()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.currencyConversionMap.value
        assertTrue(state is UiState.Error)
        assertEquals(errorMessage, (state as UiState.Error).message)
    }
} 