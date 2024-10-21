package com.team.data.network.source

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.DisplayProfileListResponse
import com.team.data.network.model.response.tag.TagListResponse
import com.team.data.network.retrofit.api.SearchNetworkApi
import com.team.data.network.source.fake.FakeSearchNetworkDataSource
import com.team.data.network.testdoubles.displayProfileListResponseTestData
import com.team.data.network.testdoubles.postsResponseTestData
import com.team.data.network.testdoubles.tagListResponseTestData
import com.team.domain.util.Result
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
class SearchNetworkDataSourceTest {

    private lateinit var searchNetworkApi: SearchNetworkApi
    private lateinit var searchNetworkDataSource: SearchNetworkDataSource
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        searchNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(SearchNetworkApi::class.java)

        searchNetworkDataSource = FakeSearchNetworkDataSource(searchNetworkApi)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `게시글 검색 (searchByPost())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(postsResponseTestData)
        })

        val expectedResponse = moshi.adapter(PostListResponse::class.java)
            .fromJson(postsResponseTestData)!!

        val actualResponse = searchNetworkDataSource.searchByPost("123", "aaa", 15)

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `프로필 검색 (searchByNickname())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(displayProfileListResponseTestData)
        })

        val expectedResponse = moshi.adapter(DisplayProfileListResponse::class.java)
            .fromJson(displayProfileListResponseTestData)!!

        val actualResponse = searchNetworkDataSource.searchByNickname("123", "aaa", 15)

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `태그 검색 (searchByTag())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(tagListResponseTestData)
        })

        val expectedResponse = moshi.adapter(TagListResponse::class.java)
            .fromJson(tagListResponseTestData)!!

        val actualResponse = searchNetworkDataSource.searchByTag("123", "aaa", 15)

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }
}