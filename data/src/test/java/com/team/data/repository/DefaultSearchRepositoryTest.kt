package com.team.data.repository

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.local.FlipDatabase
import com.team.data.local.dao.RecentSearchDao
import com.team.data.local.entity.RecentSearchEntity
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.post.toDomainModel
import com.team.data.network.model.response.profile.DisplayProfileListResponse
import com.team.data.network.model.response.profile.toDomainModel
import com.team.data.network.model.response.tag.TagListResponse
import com.team.data.network.model.response.tag.toDomainModel
import com.team.data.network.retrofit.api.SearchNetworkApi
import com.team.data.network.source.SearchNetworkDataSource
import com.team.data.network.source.fake.FakeSearchNetworkDataSource
import com.team.data.repository.fake.FakeSearchRepository
import com.team.data.testdoubles.network.displayProfileListResponseTestData
import com.team.data.testdoubles.network.displayProfileListResponseTestDataEndOfPage
import com.team.data.testdoubles.network.postsResponseTestData
import com.team.data.testdoubles.network.postsResponseTestDataEndOfPage
import com.team.data.testdoubles.network.tagListResponseTestData
import com.team.data.testdoubles.network.tagListResponseTestDataEndOfPage
import com.team.domain.repository.SearchRepository
import com.team.domain.util.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class,
)
class DefaultSearchRepositoryTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var searchNetworkDataSource: SearchNetworkDataSource
    private lateinit var searchRepository: SearchRepository

    private lateinit var searchNetworkApi: SearchNetworkApi
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Inject
    @Named("test_db")
    lateinit var database: FlipDatabase
    private lateinit var recentSearchDao: RecentSearchDao

    @Before
    fun setUp() {
        hiltRule.inject()

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

        recentSearchDao = database.recentSearchDao()
        searchNetworkDataSource = FakeSearchNetworkDataSource(searchNetworkApi)
        searchRepository = FakeSearchRepository(searchNetworkDataSource, recentSearchDao)
    }

    @After
    fun teardown() {
        server.shutdown()
        database.close()
    }

    @Test
    fun `최근 검색어 리스트 조회(Local) (getRecentSearchList())`() = runTest {
        val recentSearchEntities = listOf(
            RecentSearchEntity(word = "1"),
            RecentSearchEntity(word = "2"),
            RecentSearchEntity(word = "3"),
        )

        recentSearchEntities.forEach {
            recentSearchDao.upsertRecentSearch(it)
        }

        val results = recentSearchDao.getRecentSearchList().first()

        assert(results.isNotEmpty())
        assertEquals(results.size, recentSearchEntities.size)
    }

    @Test
    fun `최근 검색어 ID로 삭제 (deleteRecentSearchById())`() = runTest {
        recentSearchDao.clearAll()
        val recentSearchEntity = RecentSearchEntity(word = "word")
        val recentSearchEntity2 = RecentSearchEntity(word = "word2")

        recentSearchDao.upsertRecentSearch(recentSearchEntity)
        recentSearchDao.upsertRecentSearch(recentSearchEntity2)
        val firstItem = recentSearchDao.getRecentSearchList().first().first()

        recentSearchDao.deleteById(firstItem.id)

        val results = recentSearchDao.getRecentSearchList().first()

        assertEquals(results.size, 1)
    }

    @Test
    fun `최근 검색어 모두 삭제 (deleteAllRecentSearch())`() = runTest {
        val recentSearchEntities = listOf(
            RecentSearchEntity(word = "1"),
            RecentSearchEntity(word = "2"),
            RecentSearchEntity(word = "3"),
        )

        recentSearchEntities.forEach {
            recentSearchDao.upsertRecentSearch(it)
        }

        recentSearchDao.clearAll()

        val results = recentSearchDao.getRecentSearchList().first()

        assert(results.isEmpty())
    }

    @Test
    fun `게시글 검색 조회 페이지네이션 (searchByPostPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(postsResponseTestData)
        })

        val expectedResponse = moshi.adapter(PostListResponse::class.java)
            .fromJson(postsResponseTestData)!!
            .posts.toDomainModel()

        val actualResponse = searchRepository.searchByPostPagination("123", "aaa", 15).last()

        assertEquals(expectedResponse, (actualResponse as Result.Success).data.posts)
    }

    @Test
    fun `게시글 검색 조회 페이지네이션(hasNext X) (searchByPostPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(postsResponseTestDataEndOfPage)
        })

        val actualResponse = searchRepository.searchByPostPagination("123", "aaa", 15).last()

        assert(!(actualResponse as Result.Success).data.hasNext)
    }

    @Test
    fun `프로필 검색 조회 페이지네이션 (searchByNicknamePagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(displayProfileListResponseTestData)
        })

        val expectedResponse = moshi.adapter(DisplayProfileListResponse::class.java)
            .fromJson(displayProfileListResponseTestData)!!
            .profiles.toDomainModel()

        val actualResponse = searchRepository.searchByNicknamePagination("123", "aaa", 15).last()

        assertEquals(expectedResponse, (actualResponse as Result.Success).data.displayProfileList)
    }

    @Test
    fun `프로필 검색 조회 페이지네이션(hasNext X) (searchByNicknamePagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(displayProfileListResponseTestDataEndOfPage)
        })

        val actualResponse = searchRepository.searchByNicknamePagination("123", "aaa", 15).last()

        assert(!(actualResponse as Result.Success).data.hasNext)
    }

    @Test
    fun `태그 검색 조회 페이지네이션 (searchByTagPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(tagListResponseTestData)
        })

        val expectedResponse = moshi.adapter(TagListResponse::class.java)
            .fromJson(tagListResponseTestData)!!
            .tags.toDomainModel()

        val actualResponse = searchRepository.searchByTagPagination("123", "aaa", 15).last()

        assertEquals(expectedResponse, (actualResponse as Result.Success).data.tags)
    }

    @Test
    fun `태그 검색 조회 페이지네이션(hasNext X) (searchByTagPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(tagListResponseTestDataEndOfPage)
        })

        val actualResponse = searchRepository.searchByTagPagination("123", "aaa", 15).last()

        assert(!(actualResponse as Result.Success).data.hasNext)
    }
}