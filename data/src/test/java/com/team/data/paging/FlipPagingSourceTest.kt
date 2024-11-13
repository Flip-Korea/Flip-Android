package com.team.data.paging

import android.os.Build
import androidx.paging.PagingSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.ErrorBodyFactory
import com.team.data.network.networkCall
import com.team.domain.util.ErrorType
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class,
)
class FlipPagingSourceTest {

    private lateinit var apiService: FakePagingApiService
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi
    private lateinit var pagingSourceParser: PagingSourceParser
    private lateinit var errorBodyFactory: ErrorBodyFactory

    private val pagingResponseFactory = PagingResponseFactory()

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        apiService = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(FakePagingApiService::class.java)

        pagingSourceParser = PagingSourceParser(moshi)
        errorBodyFactory = ErrorBodyFactory(moshi)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `페이지네이션 호출 성공`() = runTest {
        val pageSize = 5
        val fakePagingListResponse = pagingResponseFactory.createPagingListResponse(pageSize)
        val mockResponse = pagingSourceParser.toJson(fakePagingListResponse)
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(mockResponse)
        })

        val flipPagingSource = FlipPagingSource(
            pageSize = pageSize,
            apiCall = { loadKey ->
                networkCall {
                    apiService.getPosts(loadKey?.toString(), pageSize)
                }
            }
        )

        assertEquals(
            PagingSource.LoadResult.Page(
                data = fakePagingListResponse.list,
                prevKey = null,
                nextKey = fakePagingListResponse.lastKey
            ),
            flipPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = pageSize,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `페이지네이션 호출 실패`() = runTest {
        val pageSize = 5
        val errorBody = errorBodyFactory.createObject()
        val expectedException = FlipPagingException(
            errorType = ErrorType.Network.NOT_FOUND,
            errorBody = errorBody
        )
        server.enqueue(MockResponse().apply {
            setResponseCode(404)
            setBody(errorBodyFactory.createJson())
        })

        val flipPagingSource = FlipPagingSource(
            pageSize = pageSize,
            apiCall = { loadKey ->
                networkCall {
                    apiService.getPosts(loadKey?.toString(), pageSize)
                }
            }
        )

        val result = flipPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                placeholdersEnabled = false,
                loadSize = pageSize
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)

        val throwable = (result as PagingSource.LoadResult.Error).throwable
        val actualException = throwable as FlipPagingException

        assertEquals(expectedException.errorBody, actualException.errorBody)
    }
}