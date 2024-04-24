package com.team.data.network

import android.content.Context
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

@JsonClass(generateAdapter = true)
private data class TestModel(
    @Json(name = "message") val message: String,
)

private interface ApiService {
    @GET("/test")
    suspend fun testCall(): Response<TestModel>
}

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NetworkCallTest {

    private lateinit var apiService: ApiService
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Mock
    private lateinit var context: Context

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        apiService = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `networkCall Success`() = runTest {

        val jsonTestData = """
            {
                "message": "테스트"
            }
        """.trimIndent()
        val adapter = moshi.adapter(TestModel::class.java)
        val expectedResponse = adapter.fromJson(jsonTestData)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(jsonTestData)
        })

        val networkCallResult = networkCall { apiService.testCall() }

        networkCallResult.also { actualResponse ->
            if (actualResponse is Result.Success)
                Assert.assertEquals(expectedResponse, actualResponse.data)
        }
    }

    @Test
    fun `networkCall Failure`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(404)
        })

        val networkCallResult = networkCall { apiService.testCall() }

        val expectedResponse = ErrorType.Exception.EXCEPTION
        networkCallResult.also { res ->
            if (res is Result.Error) {
                val actualResponse = res.error
                Assert.assertEquals(expectedResponse, actualResponse)
            }
        }
    }

    @Test
    fun `networkCallWithoutResponse Success`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val networkCallResult =
            com.team.data.network.networkCallWithoutResponse { apiService.testCall() }

        networkCallResult.also { actualResponse ->
            if (actualResponse is Result.Success)
                Assert.assertEquals(true, actualResponse.data)
        }
    }

    @Test
    fun `networkCallWithoutResponse Failure`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(404)
        })

        val networkCallResult = networkCall { apiService.testCall() }

        val expectedResponse = com.team.domain.util.ErrorType.Exception.EXCEPTION
        networkCallResult.also { res ->
            if (res is Result.Error) {
                val actualResponse = res.error
                Assert.assertEquals(expectedResponse, actualResponse)
            }
        }
    }
}