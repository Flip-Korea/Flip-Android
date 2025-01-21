package com.team.data.network.source

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.request.LoginRequest
import com.team.data.network.model.request.NicknameValidationRequest
import com.team.data.network.model.request.ProfileIdValidationRequest
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.model.response.account.AccountResponse
import com.team.data.network.retrofit.api.AccountNetworkApi
import com.team.data.network.source.fake.FakeAccountNetworkDataSource
import com.team.data.network.testdoubles.networkAccountJsonTestData
import com.team.data.network.testdoubles.networkRegisterTestData
import com.team.domain.type.SocialLoginPlatform
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        authNetworkApi =
            Retrofit
                .Builder()
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
    fun `getUserAccount Call Test`() =
        runTest {
            server.enqueue(
                MockResponse().apply {
                    setResponseCode(200)
                    setBody(networkAccountJsonTestData)
                },
            )

            val response = accountNetworkDataSource.getUserAccount("Bearer aaa.bbb.ccc")

            val adapter = moshi.adapter(AccountResponse::class.java)
            val mockResponseToObject = adapter.fromJson(networkAccountJsonTestData)

            assertNotNull(response)
            assertEquals(mockResponseToObject, (response as Result.Success).data)
        }

    @Test
    fun `validateName Call Test`() =
        runTest {
            // 200 OK
            server.enqueue(MockResponse().apply { setResponseCode(200) })

            val nicknameValidationRequest = NicknameValidationRequest("testNickname")
            val response = accountNetworkDataSource.validateNickname(nicknameValidationRequest)
            assertEquals(true, (response as Result.Success).data)
        }

    @Test
    fun `validateProfileId Call Test`() =
        runTest {
            // 200 OK
            server.enqueue(MockResponse().apply { setResponseCode(200) })

            val profileIdValidationRequest = ProfileIdValidationRequest("testProfileId")
            val response = accountNetworkDataSource.validateProfileId(profileIdValidationRequest)
            assertEquals(true, (response as Result.Success).data)
        }

    // login, register, tokenRefresh api는
    // Interceptor & Authenticator 추가 테스트 필요
    @Test
    fun `login Call Test`() =
        runTest {
            val adapter = moshi.adapter(TokenResponse::class.java)
            val mockResponseToObject = adapter.fromJson(TokenResponseTestData)

            server.enqueue(
                MockResponse().apply {
                    setResponseCode(200)
                    setBody(TokenResponseTestData)
                },
            )

            val loginRequest =
                LoginRequest(SocialLoginPlatform.Kakao.providerName, "kakao123test")
            val response = accountNetworkDataSource.login(loginRequest)

            assertNotNull(response)
            assertEquals(mockResponseToObject, (response as Result.Success).data)
        }

    @Test
    fun `register Call Test`() =
        runTest {
            val adapter = moshi.adapter(TokenResponse::class.java)
            val mockResponseToObject = adapter.fromJson(TokenResponseTestData)

            server.enqueue(
                MockResponse().apply {
                    setResponseCode(200)
                    setBody(TokenResponseTestData)
                },
            )

            val response = accountNetworkDataSource.register(networkRegisterTestData)

            assertNotNull(response)
            assertEquals(mockResponseToObject, (response as Result.Success).data)
        }

    @Test
    fun `tokenRefresh Call Test`() =
        runTest {
            val adapter = moshi.adapter(TokenResponse::class.java)
            val mockResponseToObject = adapter.fromJson(TokenResponseTestData)

            server.enqueue(
                MockResponse().apply {
                    setResponseCode(200)
                    setBody(TokenResponseTestData)
                },
            )

            val response = accountNetworkDataSource.tokenRefresh("Bearer aaa.bbb.ccc")

            assertNotNull(response)
            assertEquals(mockResponseToObject, (response as Result.Success).data)
        }

    companion object {
        private val TokenResponseTestData =
            """
            {
                "accessToken": "aaa.bbb.ccc",
                "refreshToken": "aaa.bbb.ccc"
            }
            """.trimIndent()
    }
}
