package com.team.data.network.source

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.ScrapCommentRequest
import com.team.data.network.model.request.ScrapRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.ProfileResponse
import com.team.data.network.retrofit.api.UserNetworkApi
import com.team.data.network.source.fake.FakeUserNetworkDataSource
import com.team.data.testdoubles.network.addScrapRequestTestData
import com.team.data.testdoubles.network.networkProfileTestData
import com.team.data.testdoubles.network.postsResponseTestDataWithScrapComment
import com.team.data.testdoubles.network.resultIdResponseTestData
import com.team.domain.util.Result
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
class UserNetworkDataSourceTest {

    private lateinit var userNetworkApi: UserNetworkApi
    private lateinit var userNetworkDataSource: UserNetworkDataSource
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

    @Test
    fun `getProfile Call Test`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkProfileTestData)
        })

        val adapter = moshi.adapter(ProfileResponse::class.java)
        val mockResponseToObject = adapter.fromJson(networkProfileTestData)

        val response = userNetworkDataSource.getProfile("testprofileid")

        assertNotNull(response)
        assertEquals(mockResponseToObject, (response as Result.Success).data)
    }

    @Test
    fun `selectMyCategory Call Test`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val response =
            userNetworkDataSource.selectMyCategory(
                profileId = "honggd",
                category = com.team.data.network.model.request.CategoryRequest(listOf(1, 2, 3))
            )

        val recordedRequest = server.takeRequest()

        val adapter = moshi.adapter(CategoryRequest::class.java)
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
            userNetworkDataSource.updateMyCategory(
                profileId = "honggd",
                category = CategoryRequest(listOf(1, 2, 3)),
            )

        val recordedRequest = server.takeRequest()

        val adapter = moshi.adapter(CategoryRequest::class.java)
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
    fun `스크랩 목록 불러오기 (getScrapList())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(postsResponseTestDataWithScrapComment)
        })

        val expectedResponse = moshi
            .adapter(PostListResponse::class.java)
            .fromJson(postsResponseTestDataWithScrapComment)

        val actualResponse =
            userNetworkDataSource.getScrapList(profileId = "honggd", cursor = "test-cursor", limit = 15)

        assertEquals(expectedResponse!!, (actualResponse as Result.Success).data)
    }

    @Test
    fun `스크랩 코멘트 수정 (editScrapComment())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val scrapCommentRequest = ScrapCommentRequest("테스트 스크랩 코멘트")
        val result =
            userNetworkDataSource.editScrapComment(
                profileId = "TestProfileId",
                scrapId = 1L,
                scrapCommentRequest = scrapCommentRequest
            )

        assert((result as Result.Success).data)
    }

    @Test
    fun `API-030 (스크랩 추가)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(resultIdResponseTestData)
        })

        val requestAdapter = moshi.adapter(ScrapRequest::class.java)
        val responseAdapter = moshi.adapter(ResultIdResponse::class.java)

        val actualResponse =
            userNetworkDataSource.addScrap(
                requestAdapter.fromJson(addScrapRequestTestData)!!
            )
        val expectedResponse = responseAdapter.fromJson(resultIdResponseTestData)

        Assert.assertNotNull(actualResponse)
        assertEquals(expectedResponse!!.resultId, (actualResponse as Result.Success).data.resultId)
    }

    @Test
    fun `API-031 (스크랩 삭제)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val actualResponse = userNetworkDataSource.deleteScrap(1)

        Assert.assertNotNull(actualResponse)
        assertEquals(true, (actualResponse as Result.Success).data)
    }
}