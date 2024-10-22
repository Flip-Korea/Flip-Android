package com.team.data.repository

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.paging.map
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.factory.TempPostFactory
import com.team.data.network.retrofit.api.PostNetworkApi
import com.team.data.network.source.PostNetworkDataSource
import com.team.data.network.source.fake.FakePostNetworkDataSource
import com.team.data.network.testdoubles.factory.TempPostResponseFactory
import com.team.data.repository.fake.FakeTempPostRepository
import com.team.data.network.testdoubles.resultIdResponseTestData
import com.team.data.paging.collectDataForTest
import com.team.domain.model.post.NewPost
import com.team.domain.model.post.TempPost
import com.team.domain.repository.TempPostRepository
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType
import com.team.domain.util.Result
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import makeTempPostListResponseTestData
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.time.Duration.Companion.seconds

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

    private val tempPostFactory = TempPostFactory()
    private val testDispatcher = UnconfinedTestDispatcher()

    private val pageSize = 15

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.isLoggable(any(), any()) } returns false

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
        tempPostRepository = FakeTempPostRepository(postNetworkDataSource, pageSize)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `플립 임시저장 글 목록 페이지네이션 (getTempPostsPagination())`() = runTest {
        val expectedList = List(pageSize) {
            tempPostFactory.create()
        }

        val result = tempPostRepository.getTempPostsPagination().first()
        val actualList = result.collectDataForTest(testDispatcher, testDispatcher)

        assertEquals(expectedList.size, actualList.size)
        assertEquals(expectedList, actualList)
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