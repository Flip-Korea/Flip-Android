package com.team.data.repository

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.FlipPagination
import com.team.data.local.FlipDatabase
import com.team.data.local.dao.PostDao
import com.team.data.local.entity.post.toExternal
import com.team.data.network.model.response.post.PostResponse
import com.team.data.network.model.response.post.toEntity
import com.team.data.network.retrofit.api.PostNetworkApi
import com.team.data.network.source.PostNetworkDataSource
import com.team.data.network.source.fake.FakePostNetworkDataSource
import com.team.data.repository.fake.FakePostRepository
import com.team.data.testdoubles.local.makeMultiplePostEntityTestData
import com.team.data.testdoubles.local.makePostIds
import com.team.data.testdoubles.network.postResponseTestData
import com.team.data.testdoubles.network.resultIdResponseTestData
import com.team.domain.model.post.NewPost
import com.team.domain.model.post.Post
import com.team.domain.type.PathParameterType
import com.team.domain.util.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
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
class DefaultPostRepositoryTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var postNetworkDataSource: PostNetworkDataSource
    private lateinit var postRepository: FakePostRepository

    private lateinit var postNetworkApi: PostNetworkApi
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Inject
    @Named("test_db")
    lateinit var database: FlipDatabase
    private lateinit var postDao: PostDao

    @Before
    fun setUp() {
        hiltRule.inject()

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

        postDao = database.postDao()

        postNetworkDataSource = FakePostNetworkDataSource(postNetworkApi)
        postRepository = FakePostRepository(postDao, postNetworkDataSource)
    }

    @After
    fun teardown() {
        server.shutdown()
        database.close()
    }

    @Test
    fun `플립 글 LocalDB에서 불러오기 (데이터 X) (getPosts())`() = runTest {
        val posts = postRepository.getPosts().first()

        assert(posts.isEmpty())
    }

    @Test
    fun `플립 글 LocalDB에서 불러오기 (데이터 O) (getPosts())`() = runTest {
        val postIds = makePostIds(5)
        val postEntities = makeMultiplePostEntityTestData(postIds)

        postDao.upsertAll(postEntities)

        val posts = postRepository.getPosts().first()

        assert(posts.contains(postEntities[0].toExternal()))
        assertEquals(posts.size, 5)
    }

    @Test
    fun `플립 글 목록 페이지네이션 (getPostsPagination())`() = runTest(UnconfinedTestDispatcher()) {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        var endOfPage = false
        var nextCursor = 1
        while (!endOfPage) {
            // FakePostNetworkDataSource - makePostListResponseTestData() -> 3개의 페이지만 반환
            val result = postRepository.getPostsPagination(nextCursor.toString()).last()
            if (!(result as Result.Success).data)
                endOfPage = true
            else
                nextCursor++
        }

        val posts = postRepository.getPosts().first()

        assertEquals(posts.size, FlipPagination.PAGE_SIZE)
        assertEquals(posts.last().createdAt, "3")
        assertEquals(nextCursor, 4)
    }

    @Test
    fun `ID로 플립 글 불러오기 (로컬캐시 X) (getPostById())`() = runTest {
        postDao.deleteAll()

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(postResponseTestData)
        })

        val postId =
            moshi
                .adapter(PostResponse::class.java)
                .fromJson(postResponseTestData)!!
                .postId

        val post = postRepository.getPostById(postId).last()

        assert((post as Result.Success).data != null)
        assertEquals(post.data!!.postId, postId)
    }

    @Test
    fun `ID로 플립 글 불러오기 (로컬캐시 O) (getPostById())`() = runTest {
        val postTestData =
            moshi
                .adapter(PostResponse::class.java)
                .fromJson(postResponseTestData)!!
                .toEntity()
        postDao.upsertPost(postTestData)

        val post = postRepository.getPostById(postTestData.postId).last()

        assert((post as Result.Success).data != null)
        assertEquals(post.data!!.postId, postTestData.postId)
    }

    @Test
    fun `플립 글 작성 (addPost())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(resultIdResponseTestData)
        })

        val newPost = NewPost(
            profileId = "testId",
            title = "testTitle",
            content = "testContent",
            createdAt = "testDate",
            categoryId = 1,
            bgColorId = 1,
            fontStyleId = 1,
            tags = listOf("a", "b", "c")
        )

        val result = postRepository.addPost(newPost).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `플립 글 수정 (editPost())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(resultIdResponseTestData)
        })

        val newPost = NewPost(
            profileId = "testId",
            title = "testTitle",
            content = "testContent",
            createdAt = "testDate",
            categoryId = 1,
            bgColorId = 1,
            fontStyleId = 1,
            tags = listOf("a", "b", "c")
        )

        val result = postRepository.editPost(newPost).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `타입 별로 플립 글 목록 페이지네이션 (getPostsByType())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        var endOfPage = false
        var nextCursor = 1

        var posts = listOf<Post>()

        while (!endOfPage) {
            // FakePostNetworkDataSource - makePostListResponseTestData() -> 3개의 페이지만 반환
            val result =
                postRepository.getPostsByTypePagination(
                    type = PathParameterType.Post.CATEGORY,
                    typeId = "2",
                    cursor = nextCursor.toString()
                ).last()
            if ((result as Result.Success).data.isEmpty()) {
                endOfPage = true
            } else {
                nextCursor++
                posts = result.data
            }
        }

        assertEquals(posts.size, FlipPagination.PAGE_SIZE)
        assertEquals(posts.last().categoryId, 2)
        assertEquals(nextCursor, 4)
    }

    @Test
    fun `플립 글 삭제 (deletePost())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val result = postRepository.deletePost(1).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `특정 분야에서 인기 플리퍼의 플립 글 목록 페이지네이션 (getPostsByPopularUserPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        var endOfPage = false
        var nextCursor = 1

        var posts = listOf<Post>()

        while (!endOfPage) {
            // FakePostNetworkDataSource - makePostListResponseTestData() -> 3개의 페이지만 반환
            val result =
                postRepository.getPostsByPopularUserPagination(
                    2,
                    nextCursor.toString(),
                    FlipPagination.PAGE_SIZE
                ).last()
            if ((result as Result.Success).data.isEmpty()) {
                endOfPage = true
            } else {
                nextCursor++
                posts = result.data
            }
        }

        assertEquals(posts.size, FlipPagination.PAGE_SIZE)
        assertEquals(posts.last().categoryId, 2)
        assertEquals(nextCursor, 4)
    }

    @Test
    fun `플립 글 좋아요 (likePost())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(resultIdResponseTestData)
        })

        val result =
            postRepository.likePost("testProfileId", 1).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `플립 글 좋아요 취소 (unLikePost())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val result =
            postRepository.unLikePost("testProfileId", 1).last()

        assert((result as Result.Success).data)
    }
}