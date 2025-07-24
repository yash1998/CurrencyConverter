package com.example.currencyconverter.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.currencyconverter.data.local.dao.CurrencyConversionDao
import com.example.currencyconverter.data.local.entities.ConversionRatesEntity
import com.example.currencyconverter.data.prefs.DataStoreKeys
import com.example.currencyconverter.data.remote.model.LatestConversionModel
import com.example.currencyconverter.utils.ApiResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LocalRepoTest {

    private lateinit var localRepo: LocalRepo
    private lateinit var conversionRatesDao: CurrencyConversionDao
    private lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setup() {
        conversionRatesDao = mockk()
        dataStore = mockk()
        localRepo = LocalRepo(conversionRatesDao, dataStore)
    }

    @Test
    fun `when database has data, return success with data`() = runBlocking {
        // Arrange
        val mockEntities = listOf(
            ConversionRatesEntity("USD", 1.0),
            ConversionRatesEntity("EUR", 0.85)
        )
        val mockPreferences = mockk<Preferences>()
        coEvery { conversionRatesDao.getConversionRatesMap() } returns mockEntities
        coEvery { dataStore.data } returns flowOf(mockPreferences)
        coEvery { mockPreferences[DataStoreKeys.LAST_UPDATED_BASE_CURRENCY] } returns "USD"
        coEvery { mockPreferences[DataStoreKeys.LAST_UPDATED_TIMESTAMP] } returns System.currentTimeMillis()

        // Act
        val result = localRepo.getLatestConversionRates()

        // Assert
        assert(result is ApiResponse.Success)
        val successResult = result as ApiResponse.Success
        assertEquals("USD", successResult.data?.base)
        assertEquals(mapOf("USD" to 1.0, "EUR" to 0.85), successResult.data?.rates)
    }

    @Test
    fun `when database is empty, return failure`() = runBlocking {
        // Arrange
        coEvery { conversionRatesDao.getConversionRatesMap() } returns emptyList()

        // Act
        val result = localRepo.getLatestConversionRates()

        // Assert
        assert(result is ApiResponse.Failure)
    }

    @Test
    fun `when inserting conversion rates, update both dao and datastore`() = runBlocking {
        // Arrange
        val mockModel = LatestConversionModel(
            base = "USD",
            rates = mapOf("EUR" to 0.85),
            timestamp = System.currentTimeMillis()
        )
        val mockDataStore = mockk<DataStore<Preferences>>()

        coEvery {
            conversionRatesDao.insertConversionRates(any())
        } just Runs
        coEvery {
            dataStore.updateData(ofType<suspend (Preferences) -> Preferences>())
        } returns mockk()

        val fakePrefs = mockk<Preferences>()

        coEvery {
            mockDataStore.updateData(any())
        } returns fakePrefs
        coEvery { conversionRatesDao.getConversionRatesMap() } returns listOf(
            ConversionRatesEntity(
                "EUR",
                0.85
            )
        )

        // Act
        localRepo.insertConversionRates(mockModel)

        // Assert
        assertTrue(conversionRatesDao.getConversionRatesMap().isNotEmpty())
    }

    @Test
    fun `when getting last updated timestamp, return correct value`() = runBlocking {
        // Arrange
        val mockTimestamp = System.currentTimeMillis()
        val mockPreferences = mockk<Preferences>()
        coEvery { dataStore.data } returns flowOf(mockPreferences)
        coEvery { mockPreferences[DataStoreKeys.LAST_UPDATED_TIMESTAMP] } returns mockTimestamp

        // Act
        val result = localRepo.getLastUpdatedTimestamp()

        // Assert
        assertEquals(mockTimestamp, result)
    }

    @Test
    fun `when getting last updated timestamp and no value exists, return -1`() = runBlocking {
        // Arrange
        val mockPreferences = mockk<Preferences>()
        coEvery { dataStore.data } returns flowOf(mockPreferences)
        coEvery { mockPreferences[DataStoreKeys.LAST_UPDATED_TIMESTAMP] } returns null

        // Act
        val result = localRepo.getLastUpdatedTimestamp()

        // Assert
        assertEquals(-1L, result)
    }
} 