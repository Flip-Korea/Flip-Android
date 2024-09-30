package com.team.data.network.retrofit.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.request.ScrapRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.profile.ProfileResponse
import com.team.data.testdoubles.network.addScrapRequestTestData
import com.team.data.testdoubles.network.networkProfileTestData
import com.team.data.testdoubles.network.resultIdResponseTestData
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class UserNetworkApiTest {

    private lateinit var userNetworkApi: UserNetworkApi
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
            .create(UserNetworkApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getProfile Call Test`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkProfileTestData)
        })

        val adapter = moshi.adapter(ProfileResponse::class.java)
        val mockResponseToObject = adapter.fromJson(networkProfileTestData)

        val response = userNetworkApi.getProfile("testprofileid")

        assertNotNull(response)
        assertEquals(200, response.code())
        assertEquals(mockResponseToObject, response.body()!!)
    }

    @Test
    fun `selectMyCategory Call Test`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val response =
            userNetworkApi.selectMyCategory(
                profileId = "honggd",
                category = com.team.data.network.model.request.CategoryRequest(listOf(1, 2, 3))
            )

        val recordedRequest = server.takeRequest()

        val adapter = moshi.adapter(com.team.data.network.model.request.CategoryRequest::class.java)
        val realRequestBody = adapter.fromJson(recordedRequest.body.peek())
        val requestBody = """
            {
                "categoryIds": [1,2,3]
            }
        """.trimIndent()
        val expectedRequestBody = adapter.fromJson(requestBody)

        assertNotNull(response.body())
        assertEquals(201, response.code())
        assertEquals(expectedRequestBody!!.categoryIds, realRequestBody!!.categoryIds)
    }

    @Test
    fun `API-030 (스크랩 추가)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(resultIdResponseTestData)
        })

        val requestAdapter = moshi.adapter(ScrapRequest::class.java)
        val responseAdapter = moshi.adapter(ResultIdResponse::class.java)

        val actualResponse = userNetworkApi.addScrap(
            requestAdapter.fromJson(addScrapRequestTestData)!!
        )
        val expectedResponse = responseAdapter.fromJson(resultIdResponseTestData)

        Assert.assertNotNull(actualResponse)
        assertEquals(201, actualResponse.code())
        assertEquals(expectedResponse!!.resultId, 123)
    }

    @Test
    fun `API-031 (스크랩 삭제)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val actualResponse = userNetworkApi.deleteScrap(1)

        Assert.assertNotNull(actualResponse)
        assertEquals(200, actualResponse.code())
    }
}