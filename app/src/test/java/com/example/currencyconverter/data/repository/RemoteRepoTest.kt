package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.remote.CurrencyConverterApiService
import com.example.currencyconverter.utils.ApiResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import kotlinx.coroutines.runBlocking

@RunWith(JUnit4::class)
class RemoteRepoTest {

    private lateinit var remoteRepo: RemoteRepo
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: CurrencyConverterApiService
    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val contentType = "application/json".toMediaType()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(CurrencyConverterApiService::class.java)
        remoteRepo = RemoteRepo(apiService)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `when getLatestConversionRates returns successful response, return success with data`() = runBlocking {
        // Arrange
        val mockResponse = """
            {
                "base": "USD",
                "rates": {
                    "EUR": 0.85,
                    "GBP": 0.73
                },
                "timestamp": 1234567890
            }
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        // Act
        val result = remoteRepo.getLatestConversionRates()

        // Assert
        assertTrue(result is ApiResponse.Success)
        val successResult = result as ApiResponse.Success
        assertEquals("USD", successResult.data?.base)
        assertEquals(0.85, successResult.data?.rates?.get("EUR"))
        assertEquals(0.73, successResult.data?.rates?.get("GBP"))
    }

    @Test
    fun `when getLatestConversionRates returns error response, return failure`() = runBlocking {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        // Act
        val result = remoteRepo.getLatestConversionRates()

        // Assert
        assertTrue(result is ApiResponse.Failure)
    }

    @Test
    fun `when getLatestConversionRates throws exception, return failure`() = runBlocking {
        // Arrange
        mockWebServer.enqueue(MockResponse().setBody("invalid json"))

        // Act
        val result = remoteRepo.getLatestConversionRates()

        // Assert
        assertTrue(result is ApiResponse.Failure)
    }

    @Test
    fun `when getCurrencyList returns successful response, return success with data`() = runBlocking {
        // Arrange
        val mockResponse = """
            {
                "USD": "US Dollar",
                "EUR": "Euro",
                "GBP": "British Pound"
            }
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        // Act
        val result = remoteRepo.getCurrencyList()

        // Assert
        assertTrue(result is ApiResponse.Success)
        val successResult = result as ApiResponse.Success
        assertEquals("US Dollar", successResult.data?.get("USD"))
        assertEquals("Euro", successResult.data?.get("EUR"))
        assertEquals("British Pound", successResult.data?.get("GBP"))
    }

    @Test
    fun `when getCurrencyList returns error response, return failure`() = runBlocking {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        // Act
        val result = remoteRepo.getCurrencyList()

        // Assert
        assertTrue(result is ApiResponse.Failure)
    }

    @Test
    fun `when getCurrencyList throws exception, return failure`() = runBlocking {
        // Arrange
        mockWebServer.enqueue(MockResponse().setBody("invalid json"))

        // Act
        val result = remoteRepo.getCurrencyList()

        // Assert
        assertTrue(result is ApiResponse.Failure)
    }
} 