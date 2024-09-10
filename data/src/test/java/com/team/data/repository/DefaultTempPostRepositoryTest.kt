package com.team.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.retrofit.api.PostNetworkApi
import com.team.data.network.source.PostNetworkDataSource
import com.team.data.network.source.fake.FakePostNetworkDataSource
import com.team.data.repository.fake.FakeTempPostRepository
import com.team.data.testdoubles.network.resultIdResponseTestData
import com.team.domain.model.post.NewPost
import com.team.domain.repository.TempPostRepository
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import makeTempPostListResponseTestData
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DefaultTempPostRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var postNetworkDataSource: PostNetworkDataSource
    private lateinit var tempPostRepository: TempPostRepository

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
        tempPostRepository = FakeTempPostRepository(postNetworkDataSource)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `플립 임시저장 글 목록 페이지네이션 (getTempPostsPagination())`() = runTest {

        val pageSize = 15

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(makeTempPostListResponseTestData("1", pageSize))
        })

        val result =
            tempPostRepository.getTempPostsPagination("1", pageSize).last()

        Assert.assertEquals(pageSize, (result as Result.Success).data.tempPosts.size)
    }

    @Test
    fun `플립 임시저장 글 추가 (addTemporaryPost())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(resultIdResponseTestData)
        })

        val newPost = NewPost(
            title = "title",
            content = "content",
            categoryId = 1,
            bgColorType = BackgroundColorType.RED,
            fontStyleType = FontStyleType.NORMAL,
            tags = listOf("a", "b")
        )

        val result = tempPostRepository.addTemporaryPost(newPost).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `플립 임시저장 글 삭제 (deleteTemporaryPost())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val result = tempPostRepository.deleteTemporaryPost(1).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `플립 임시저장 글 수정 (editTemporaryPost())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val newPost = NewPost(
            title = "title",
            content = "content",
            categoryId = 1,
            bgColorType = BackgroundColorType.RED,
            fontStyleType = FontStyleType.NORMAL,
            tags = listOf("a", "b")
        )

        val result = tempPostRepository.editTemporaryPost(1, newPost).last()

        assert((result as Result.Success).data)
    }
}