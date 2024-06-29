package com.team.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.domain.util.FlipPagination
import com.team.data.network.retrofit.api.UserNetworkApi
import com.team.data.network.source.UserNetworkDataSource
import com.team.data.network.source.fake.FakeUserNetworkDataSource
import com.team.data.repository.fake.FakeScrapRepository
import com.team.data.testdoubles.network.resultIdResponseTestData
import com.team.domain.model.scrap.NewScrap
import com.team.domain.repository.ScrapRepository
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import makePostListResponseTestData
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
class DefaultScrapRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userNetworkDataSource: UserNetworkDataSource
    private lateinit var scrapRepository: ScrapRepository

    private lateinit var userNetworkApi: UserNetworkApi
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        userNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(UserNetworkApi::class.java)

        userNetworkDataSource = FakeUserNetworkDataSource(userNetworkApi)
        scrapRepository = FakeScrapRepository(userNetworkDataSource)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `스크랩 목록 페이지네이션 (getScrapListPagination())`() = runTest {

        val pageSize = 15

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(makePostListResponseTestData("1", pageSize))
        })

        val result =
            scrapRepository.getScrapListPagination(
                "TestProfileId",
                "1",
                FlipPagination.PAGE_SIZE
            ).last()

        assertEquals(pageSize, (result as Result.Success).data.posts.size)
    }

    @Test
    fun `스크랩 코멘트 수정 (editScrapComment())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val newComment = "수정된 코멘트"

        val result =
            scrapRepository.editScrapComment("TestProfileId", 1, newComment).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `스크랩 추가 (addScrap())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(resultIdResponseTestData)
        })

        val newScrap = NewScrap(
            "TestProfileId",
            1,
            "코멘트!"
        )

        val result = scrapRepository.addScrap(newScrap).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `스크랩 삭제 (deleteScrap())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val result = scrapRepository.deleteScrap(1).last()

        assert((result as Result.Success).data)
    }
}