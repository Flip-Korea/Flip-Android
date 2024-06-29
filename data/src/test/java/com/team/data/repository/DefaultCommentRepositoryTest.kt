package com.team.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.domain.util.FlipPagination
import com.team.data.network.retrofit.api.PostNetworkApi
import com.team.data.network.source.PostNetworkDataSource
import com.team.data.network.source.fake.FakePostNetworkDataSource
import com.team.data.repository.fake.FakeCommentRepository
import com.team.data.testdoubles.network.resultIdResponseTestData
import com.team.domain.model.comment.NewComment
import com.team.domain.repository.CommentRepository
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import makeCommentListResponseTestData
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DefaultCommentRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var postNetworkDataSource: PostNetworkDataSource
    private lateinit var commentRepository: CommentRepository

    private lateinit var postNetworkApi: PostNetworkApi
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
        commentRepository = FakeCommentRepository(postNetworkDataSource)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `댓글 목록 페이지네이션 (getCommentsPagination())`() = runTest {

        val pageSize = 15

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(makeCommentListResponseTestData(1, "3", pageSize))
        })

        val result = commentRepository.getCommentsPagination(1, "2", FlipPagination.PAGE_SIZE).last()

        assertEquals(pageSize, (result as Result.Success).data.comments.size)
    }

    @Test
    fun `댓글 추가 (addCommnet())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(resultIdResponseTestData)
        })

        val postId = 1
        val newComment = NewComment(
            profileId = "TestProfileId",
            postId = postId.toLong(),
            comment = "테스트 댓글"
        )

        val result =
            commentRepository.addComment(1, newComment).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `댓글 삭제 (deleteComment())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val commentId = 1

        val result =
            commentRepository.deleteComment(commentId.toLong()).last()

        assert((result as Result.Success).data)
    }
}