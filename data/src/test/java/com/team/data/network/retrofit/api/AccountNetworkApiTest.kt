package com.team.data.network.retrofit.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.response.account.AccountResponse
import com.team.data.testdoubles.network.networkAccountJsonTestData
import com.team.data.testdoubles.network.networkRegisterTestData
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AccountNetworkApiTest {

    private lateinit var accountNetworkApi: AccountNetworkApi
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        accountNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(AccountNetworkApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getUserAccount Call Test`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkAccountJsonTestData)
        })

        val response = accountNetworkApi.getUserAccount("Bearer aaa.bbb.ccc")

//        val request = server.takeRequest()

        val adapter = moshi.adapter(AccountResponse::class.java)
        val mockResponseToObject = adapter.fromJson(networkAccountJsonTestData)

        assertNotNull(response.body())
        assertEquals(200, response.code())
        assertEquals(mockResponseToObject, response.body()!!)
    }

    @Test
    fun `checkDuplicateName Call Test`() = runTest {

        // 200 OK
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })
        val response = accountNetworkApi.checkDuplicateName("testNickname")
        assertEquals(200, response.code())

        // 409 Conflict
        server.enqueue(MockResponse().apply {
            setResponseCode(409)
        })
        val response2 = accountNetworkApi.checkDuplicateName("testNickname")
        assertEquals(409, response2.code())
    }

    @Test
    fun `checkDuplicateProfileId Call Test`() = runTest {

        // 200 OK
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })
        val response = accountNetworkApi.checkDuplicateProfileId("testAccountId")
        assertEquals(200, response.code())

        // 409 Conflict
        server.enqueue(MockResponse().apply {
            setResponseCode(409)
        })
        val response2 = accountNetworkApi.checkDuplicateProfileId("testAccountId")
        assertEquals(409, response2.code())
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

        val response = accountNetworkApi.login("kakao123test")

        assertNotNull(response.body())
        assertEquals(200, response.code())
        assertEquals(mockResponseToObject, response.body()!!)
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

        val response = accountNetworkApi.register(networkRegisterTestData)

        assertNotNull(response.body())
        assertEquals(200, response.code())
        assertEquals(mockResponseToObject, response.body()!!)
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

        val realResponse = accountNetworkApi.tokenRefresh("Bearer aaa.bbb.ccc")

        assertNotNull(realResponse.body())
        assertEquals(200, realResponse.code())
        assertEquals(mockResponseToObject, realResponse.body()!!)
    }
}