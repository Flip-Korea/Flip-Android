package com.team.data.network.source

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.model.response.account.AccountResponse
import com.team.data.network.retrofit.api.AccountNetworkApi
import com.team.data.testdoubles.network.networkAccountJsonTestData
import com.team.data.testdoubles.network.networkRegisterTestData
import com.team.data.network.source.fake.FakeAccountNetworkDataSource
import com.team.domain.util.Result
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
class AccountNetworkDataSourceTest {

    private lateinit var authNetworkApi: AccountNetworkApi
    private lateinit var accountNetworkDataSource: AccountNetworkDataSource
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
            .create(AccountNetworkApi::class.java)

        accountNetworkDataSource = FakeAccountNetworkDataSource(authNetworkApi)
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

        val response = accountNetworkDataSource.getUserAccount("Bearer aaa.bbb.ccc")

        val adapter = moshi.adapter(AccountResponse::class.java)
        val mockResponseToObject = adapter.fromJson(networkAccountJsonTestData)

        assertNotNull(response)
        assertEquals(mockResponseToObject, (response as Result.Success).data)
    }

    @Test
    fun `checkDuplicateName Call Test`() = runTest {

        // 200 OK
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val response = accountNetworkDataSource.checkDuplicateName("testNickname")
        assertEquals(true, (response as Result.Success).data)
    }

    @Test
    fun `checkDuplicateProfileId Call Test`() = runTest {

        // 200 OK
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val response = accountNetworkDataSource.checkDuplicateProfileId("testProfileId")
        assertEquals(true, (response as Result.Success).data)
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
        val adapter = moshi.adapter(TokenResponse::class.java)
        val mockResponseToObject = adapter.fromJson(mockResponse)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(mockResponse)
        })

        val response = accountNetworkDataSource.login("kakao123test")

        assertNotNull(response)
        assertEquals(mockResponseToObject, (response as Result.Success).data)
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
        val adapter = moshi.adapter(TokenResponse::class.java)
        val mockResponseToObject = adapter.fromJson(mockResponse)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(mockResponse)
        })

        val response = accountNetworkDataSource.register(networkRegisterTestData)

        assertNotNull(response)
        assertEquals(mockResponseToObject, (response as Result.Success).data)
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
        val adapter = moshi.adapter(TokenResponse::class.java)
        val mockResponseToObject = adapter.fromJson(mockResponse)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(mockResponse)
        })

        val response = accountNetworkDataSource.tokenRefresh("Bearer aaa.bbb.ccc")

        assertNotNull(response)
        assertEquals(mockResponseToObject, (response as Result.Success).data)
    }
}