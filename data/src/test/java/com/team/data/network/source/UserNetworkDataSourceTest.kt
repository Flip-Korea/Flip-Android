package com.team.data.network.source

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.testdoubles.network.model.networkProfileTestData
import com.team.data.testdoubles.network.source.FakeUserNetworkDataSource
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
class UserNetworkDataSourceTest {

    private lateinit var userNetworkApi: com.team.data.network.retrofit.api.UserNetworkApi
    private lateinit var userNetworkDataSource: com.team.data.network.source.UserNetworkDataSource
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        userNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(com.team.data.network.retrofit.api.UserNetworkApi::class.java)

        userNetworkDataSource = FakeUserNetworkDataSource(userNetworkApi)
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
    fun `getProfile Call Test`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkProfileTestData)
        })

        val adapter = moshi.adapter(com.team.data.network.model.response.ProfileResponse::class.java)
        val mockResponseToObject = adapter.fromJson(networkProfileTestData)

        val response = getResult(userNetworkDataSource.getProfile("testprofileid"))

        assertNotNull(response)
        assertEquals(mockResponseToObject, response)
    }

    @Test
    fun `selectMyCategory Call Test`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val response =
            getResult(userNetworkDataSource.selectMyCategory(
                profileId = "honggd",
                category = com.team.data.network.model.request.CategoryRequest(listOf(1, 2, 3))
            ))

        val recordedRequest = server.takeRequest()

        val adapter = moshi.adapter(com.team.data.network.model.request.CategoryRequest::class.java)
        val realRequestBody = adapter.fromJson(recordedRequest.body.peek())
        val requestBody = """
            {
                "categories": [1,2,3]
            }
        """.trimIndent()
        val expectedRequestBody = adapter.fromJson(requestBody)

        assertNotNull(response)
        assertEquals(expectedRequestBody!!.categories, realRequestBody!!.categories)
    }

    @Test
    fun `updateMyCategory Call Test`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val response =
            getResult(userNetworkDataSource.updateMyCategory(
                profileId = "honggd",
                category = com.team.data.network.model.request.CategoryRequest(listOf(1, 2, 3)),
                accessToken = "Bearer aaa.bbb.ccc"
            ))

        val recordedRequest = server.takeRequest()

        val adapter = moshi.adapter(com.team.data.network.model.request.CategoryRequest::class.java)
        val realRequestBody = adapter.fromJson(recordedRequest.body.peek())
        val requestBody = """
            {
                "categories": [1,2,3]
            }
        """.trimIndent()
        val expectedRequestBody = adapter.fromJson(requestBody)

        assertNotNull(response)
        assertEquals(expectedRequestBody!!.categories, realRequestBody!!.categories)
    }
}