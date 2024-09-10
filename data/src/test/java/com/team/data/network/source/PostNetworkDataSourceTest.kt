package com.team.data.network.source

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.request.CommentRequest
import com.team.data.network.model.request.LikeRequest
import com.team.data.network.model.request.PostRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.comment.CommentListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.post.PostResponse
import com.team.data.network.model.response.post.TempPostListResponse
import com.team.data.network.retrofit.api.PostNetworkApi
import com.team.data.network.source.fake.FakePostNetworkDataSource
import com.team.data.testdoubles.network.commentRequestTestData
import com.team.data.testdoubles.network.commentResponseTestData
import com.team.data.testdoubles.network.likePostRequestTestData
import com.team.data.testdoubles.network.postRequestTestData
import com.team.data.testdoubles.network.postResponseTestData
import com.team.data.testdoubles.network.postsResponseTestData
import com.team.data.testdoubles.network.resultIdResponseTestData
import com.team.data.testdoubles.network.tempPostListResponseTestData
import com.team.domain.type.PathParameterType
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
class PostNetworkDataSourceTest {

    private lateinit var postNetworkApi: PostNetworkApi
    private lateinit var postNetworkDataSource: PostNetworkDataSource
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        postNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(PostNetworkApi::class.java)

        postNetworkDataSource = FakePostNetworkDataSource(postNetworkApi)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `API-013 (모든 게시글 조회)`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(postsResponseTestData)
        })

        val expectedResponse = moshi
            .adapter(PostListResponse::class.java)
            .fromJson(postsResponseTestData)
        val actualResponse = postNetworkDataSource.getPosts("0001", 15)

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `API-051 (단일 게시글 조회)`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(postResponseTestData)
        })

        val adapter = moshi.adapter(PostResponse::class.java)
        val expectedResponse = adapter.fromJson(postResponseTestData)

        val actualResponse = postNetworkDataSource.getPostById(12345)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
        assertEquals(expectedResponse!!.postId, actualResponse.data.postId)
    }

    @Test
    fun `API-014 (게시글 작성)`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val requestAdapter = moshi.adapter(PostRequest::class.java)

        val actualResponse = postNetworkDataSource.addPost(requestAdapter.fromJson(
            postRequestTestData
        )!!)

        assertNotNull(actualResponse)
        assert((actualResponse as Result.Success).data)
    }

    @Test
    fun ` API-052 (게시글 편집)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val requestAdapter = moshi.adapter(PostRequest::class.java)

        val actualResponse = postNetworkDataSource.editPost(requestAdapter.fromJson(
            postRequestTestData
        )!!)

        assertNotNull(actualResponse)
        assertEquals(true, (actualResponse as Result.Success).data)
    }

    @Test
    fun `API-015 (카테고리, 회원, 태그 별 게시글 조회)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(postsResponseTestData)
        })

        val adapter = moshi.adapter(PostListResponse::class.java)
        val expectedResponse = adapter.fromJson(postsResponseTestData)

        val actualResponse =
            postNetworkDataSource.getPostsByType(
                type = PathParameterType.Post.CATEGORY,
                typeId = "1",
                cursor = "0001",
                limit = 15
            )

        assertNotNull(actualResponse)
        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `API-017 (게시글 삭제)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val actualResponse = postNetworkDataSource.deletePost(1)
        assertNotNull(actualResponse)
        assertEquals(true, (actualResponse as Result.Success).data)
    }

    @Test
    fun `API-021 (특정 분야(카테고리)에서 인기 플리퍼 게시글 조회)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(postsResponseTestData)
        })

        val adapter = moshi.adapter(PostListResponse::class.java)
        val expectedResponse = adapter.fromJson(postsResponseTestData)

        val actualResponse = postNetworkDataSource.getPostsByPopularUser(1, "0001", 15)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `API-023 (댓글 조회)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(commentResponseTestData)
        })

        val adapter = moshi.adapter(CommentListResponse::class.java)
        val expectedResponse = adapter.fromJson(commentResponseTestData)

        val actualResponse = postNetworkDataSource.getComments(1, "0001", 15)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `API-024 (댓글 작성)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(resultIdResponseTestData)
        })

        val requestAdapter = moshi.adapter(CommentRequest::class.java)
        val responseAdapter = moshi.adapter(ResultIdResponse::class.java)

        val actualResponse =
            postNetworkDataSource.addComment(
                1,
                requestAdapter.fromJson(commentRequestTestData)!!
            )
        val expectedResponse = responseAdapter.fromJson(resultIdResponseTestData)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse!!.resultId, (actualResponse as Result.Success).data.resultId)
    }

    @Test
    fun `API-029 (댓글 삭제(본인만))`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val actualResponse = postNetworkDataSource.deleteComment(1)

        assertNotNull(actualResponse)
        assertEquals(true, (actualResponse as Result.Success).data)
    }

    @Test
    fun `API-027 (게시글 좋아요)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(resultIdResponseTestData)
        })

        val requestAdapter = moshi.adapter(LikeRequest::class.java)
        val responseAdapter = moshi.adapter(ResultIdResponse::class.java)

        val actualResponse =
            postNetworkDataSource.likePost(
                requestAdapter.fromJson(likePostRequestTestData)!!
            )
        val expectedResponse = responseAdapter.fromJson(resultIdResponseTestData)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse!!.resultId, (actualResponse as Result.Success).data.resultId)
    }

    @Test
    fun `API-028 (게시글 좋아요 취소)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val requestAdapter = moshi.adapter(LikeRequest::class.java)

        val actualResponse =
            postNetworkDataSource.unLikePost(
                requestAdapter.fromJson(likePostRequestTestData)!!
            )

        assertNotNull(actualResponse)
        assertEquals(true, (actualResponse as Result.Success).data)
    }

    @Test
    fun `API-032 (임시저장 게시글 추가)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val requestAdapter = moshi.adapter(PostRequest::class.java)
        val responseAdapter = moshi.adapter(ResultIdResponse::class.java)

        val actualResponse =
            postNetworkDataSource.addTemporaryPost(requestAdapter.fromJson(postRequestTestData)!!)

        assertNotNull(actualResponse)
        assert((actualResponse as Result.Success).data)
    }

    @Test
    fun `API-033 (임시저장 게시글 삭제)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val actualResponse = postNetworkDataSource.deleteTemporaryPost(1)

        assertNotNull(actualResponse)
        assertEquals(true, (actualResponse as Result.Success).data)
    }

    @Test
    fun `API-034 (임시저장 게시글 조회)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(tempPostListResponseTestData)
        })

        val adapter = moshi.adapter(TempPostListResponse::class.java)
        val expectedResponse = adapter.fromJson(tempPostListResponseTestData)

        val actualResponse = postNetworkDataSource.getTemporaryPosts("0001", 15)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `API-035 (임시저장 게시글 편집)`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val adapter = moshi.adapter(PostRequest::class.java)

        val actualResponse =
            postNetworkDataSource.editTemporaryPost(1, adapter.fromJson(postRequestTestData)!!)

        assertNotNull(actualResponse)
        assertEquals(true, (actualResponse as Result.Success).data)
    }
}