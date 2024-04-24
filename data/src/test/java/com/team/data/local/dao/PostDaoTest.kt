package com.team.data.local.dao

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team.data.local.FlipDatabase
import com.team.data.testdoubles.local.makePostEntityTestData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class,
//    instrumentedPackages = [
//        // required to access final members on androidx.loader.content.ModernAsyncTask
//        "androidx.loader.content"
//    ])
)
class PostDaoTest {

    @get:Rule(order = 1)
    var hiltModule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    // 안드로이드 구성요소 관련 작업들을 모두 한 스레드에서 실행되게 함
    // 모든 코드가 동기적으로 실행

    @Inject
    @Named("test_db")
    lateinit var database: FlipDatabase
    private lateinit var postDao: PostDao

    @Before
    fun setUp() {
        hiltModule.inject()
        postDao = database.postDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `모든 플립 글 불러오기 (getPosts())`() = runTest {

        val postId = Random.nextLong(1, 50000)
        val postId2 = Random.nextLong(1, 50000)
        val postEntity = makePostEntityTestData(postId)
        val postEntity2 = makePostEntityTestData(postId2)

        postDao.upsertPost(postEntity)
        postDao.upsertPost(postEntity2)

        val posts = postDao.getPosts().first()

        assertEquals(posts.size, 2)
    }

    @Test
    fun `플립 글 1개 불러오기 (getPostById())`() = runTest {

        val postId = Random.nextLong(1, 50000)
        val postEntity = makePostEntityTestData(postId)

        postDao.upsertPost(postEntity)

        val post = postDao.getPostById(postId).first()

        assert(post != null)
        assertEquals(post!!.postId, postId)
    }

    @Test
    fun `플립 글 추가 (upsertPost())`() = runTest {

        val postId = Random.nextLong(1, 50000)
        val postEntity = makePostEntityTestData(postId)

        postDao.upsertPost(postEntity)

        val post = postDao.getPostById(postId).first()

        assert(post != null)
        assertEquals(post!!.postId, postId)
    }

    @Test
    fun `여러 플립 글 한 번에 추가 (upsertAll())`() = runTest {

        val postId = Random.nextLong(1, 50000)
        val postId2 = Random.nextLong(1, 50000)
        val postEntity = makePostEntityTestData(postId)
        val postEntity2 = makePostEntityTestData(postId2)

        val postEntities = listOf(postEntity, postEntity2)

        postDao.upsertAll(postEntities)

        val posts = postDao.getPosts().first()

        assertEquals(posts.size, postEntities.size)
        assert(posts.contains(postEntity))
        assert(posts.contains(postEntity2))
    }

    @Test
    fun `플립 글 삭제 (deletePost())`() = runTest {

        val postId = Random.nextLong(1, 50000)
        val postId2 = Random.nextLong(1, 50000)
        val postEntity = makePostEntityTestData(postId)
        val postEntity2 = makePostEntityTestData(postId2)

        val postEntities = listOf(postEntity, postEntity2)

        postDao.upsertAll(postEntities)
        postDao.deletePost(postEntity2)

        val posts = postDao.getPosts().first()

        assertEquals(posts.size, 1)
        assertEquals(posts[0].postId, postEntity.postId)
    }

    @Test
    fun `모든 플립 글 삭제 (deleteAll())`() = runTest {

        val postId = Random.nextLong(1, 50000)
        val postId2 = Random.nextLong(1, 50000)
        val postEntity = makePostEntityTestData(postId)
        val postEntity2 = makePostEntityTestData(postId2)

        val postEntities = listOf(postEntity, postEntity2)

        postDao.upsertAll(postEntities)
        postDao.deleteAll()

        assert(postDao.getPosts().first().isEmpty())
    }

    @Test
    fun `새로고침 (refresh())`() = runTest {

        val postId = Random.nextLong(1, 50000)
        val postId2 = Random.nextLong(1, 50000)
        val postEntity = makePostEntityTestData(postId)
        val postEntity2 = makePostEntityTestData(postId2)

        val postEntities = listOf(postEntity, postEntity2)

        postDao.upsertAll(postEntities)
        repeat(2) { postDao.refresh(postEntities) }

        val posts = postDao.getPosts().first()

        assertEquals(posts.size, 2)
    }
}