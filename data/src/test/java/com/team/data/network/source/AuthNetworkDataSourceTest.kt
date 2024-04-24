package com.team.data.network.source

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.testdoubles.network.model.networkAccountJsonTestData
import com.team.data.testdoubles.network.model.networkRegisterTestData
import com.team.data.testdoubles.network.source.FakeAuthNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AuthNetworkDataSourceTest {

    private lateinit var authNetworkApi: com.team.data.network.retrofit.api.AuthNetworkApi
    private lateinit var authNetworkDataSource: com.team.data.network.source.AuthNetworkDataSource
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        authNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(com.team.data.network.retrofit.api.AuthNetworkApi::class.java)

        authNetworkDataSource = FakeAuthNetworkDataSource(authNetworkApi)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private suspend fun <T> getResult(response: Flow<Result<T, ErrorType>>): T? {
        return when (val result = response.first()) {
            is Result.Error -> { null }
            Result.Loading -> { null }
            is Result.Success -> { result.data }
        }
    }

    @Test
    fun `getUserAccount Call Test`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkAccountJsonTestData)
        })

        val response = getResult(authNetworkDataSource.getUserAccount("Bearer aaa.bbb.ccc"))

        val adapter = moshi.adapter(com.team.data.network.model.response.AccountResponse::class.java)
        val mockResponseToObject = adapter.fromJson(networkAccountJsonTestData)

        assertNotNull(response)
        assertEquals(mockResponseToObject, response)
    }

    @Test
    fun `checkDuplicateName Call Test`() = runTest {

        // 200 OK
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val response = getResult(authNetworkDataSource.checkDuplicateName("testNickname"))
        assertEquals(true, response)
    }

    @Test
    fun `checkDuplicateAccountId Call Test`() = runTest {

        // 200 OK
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val response = getResult(authNetworkDataSource.checkDuplicateAccountId("testAccountId"))
        assertEquals(true, response)
    }

    // login, register, tokenRefresh api는
    // Interceptor & Authenticator 추가 테스트 필요
    @Test
    fun `login Call Test`() = runTest {

        // Mock Data
        val mockResponse = """
            {
                "access_token": "aaa.bbb.ccc",
                "refresh_token": "aaa.bbb.ccc"
            }
        """.trimIndent()
        val adapter = moshi.adapter(com.team.data.network.model.response.TokenResponse::class.java)
        val mockResponseToObject = adapter.fromJson(mockResponse)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(mockResponse)
        })

        val response = getResult(authNetworkDataSource.login("kakao123test"))

        assertNotNull(response)
        assertEquals(mockResponseToObject, response)
    }

    @Test
    fun `register Call Test`() = runTest {

        // Mock Data
        val mockResponse = """
            {
                "access_token": "aaa.bbb.ccc",
                "refresh_token": "aaa.bbb.ccc"
            }
        """.trimIndent()
        val adapter = moshi.adapter(com.team.data.network.model.response.TokenResponse::class.java)
        val mockResponseToObject = adapter.fromJson(mockResponse)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(mockResponse)
        })

        val response = getResult(authNetworkDataSource.register(networkRegisterTestData))

        assertNotNull(response)
        assertEquals(mockResponseToObject, response)
    }

    @Test
    fun `tokenRefresh Call Test`() = runTest {

        // Mock Data
        val mockResponse = """
            {
                "access_token": "aaa.bbb.ccc",
                "refresh_token": "aaa.bbb.ccc"
            }
        """.trimIndent()
        val adapter = moshi.adapter(com.team.data.network.model.response.TokenResponse::class.java)
        val mockResponseToObject = adapter.fromJson(mockResponse)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(mockResponse)
        })

        val realResponse = getResult(authNetworkDataSource.tokenRefresh("Bearer aaa.bbb.ccc"))

        assertNotNull(realResponse)
        assertEquals(mockResponseToObject, realResponse)
    }
}