package com.team.data.repository

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.di.IODispatcher
import com.team.data.local.FlipDatabase
import com.team.data.local.dao.MyProfileDao
import com.team.data.network.model.response.block.BlockListResponse
import com.team.data.network.model.response.block.toDomainModel
import com.team.data.network.model.response.comment.MyCommentListResponse
import com.team.data.network.model.response.comment.toDomainModel
import com.team.data.network.model.response.follow.FollowerListResponse
import com.team.data.network.model.response.follow.FollowingListResponse
import com.team.data.network.model.response.profile.ProfileResponse
import com.team.data.network.model.response.profile.toDomainModel
import com.team.data.network.retrofit.api.UserNetworkApi
import com.team.data.network.source.UserNetworkDataSource
import com.team.data.network.source.fake.FakeUserNetworkDataSource
import com.team.data.repository.fake.FakeUserRepository
import com.team.data.testdoubles.local.makeMyProfileEntityTestData
import com.team.data.testdoubles.network.makeNetworkMyProfileTestData
import com.team.data.testdoubles.network.networkBlocksTestData
import com.team.data.testdoubles.network.networkBlocksTestDataEndOfPage
import com.team.data.testdoubles.network.networkFollowersTestData
import com.team.data.testdoubles.network.networkFollowersTestDataEndOfPage
import com.team.data.testdoubles.network.networkFollowingsTestData
import com.team.data.testdoubles.network.networkFollowingsTestDataEndOfPage
import com.team.data.testdoubles.network.networkMyCommentsTestData
import com.team.data.testdoubles.network.networkMyCommentsTestDataEndOfPage
import com.team.data.testdoubles.network.networkProfileTestData
import com.team.domain.model.profile.EditProfile
import com.team.domain.model.report_block.BlockReq
import com.team.domain.model.report_block.ReportReq
import com.team.domain.type.ReportType
import com.team.domain.util.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.Assert
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
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
class DefaultUserRepositoryTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userNetworkDataSource: UserNetworkDataSource
    private lateinit var userRepository: FakeUserRepository

    private lateinit var userNetworkApi: UserNetworkApi
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Inject
    @Named("test_db")
    lateinit var database: FlipDatabase
    private lateinit var myProfileDao: MyProfileDao

    @Inject
    @IODispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        hiltRule.inject()

        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        userNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(UserNetworkApi::class.java)

        myProfileDao = database.myProfileDao()
        userNetworkDataSource = FakeUserNetworkDataSource(userNetworkApi)
        userRepository = FakeUserRepository(userNetworkDataSource, myProfileDao, ioDispatcher)
    }

    @After
    fun teardown() {
        server.shutdown()
        database.close()
    }

    @Test
    fun `내 프로필 새로고침 (getMyProfileRefresh())`() = runTest {

        val profileId = "profileId(test)"
        // 기존에 MyProfileEntity가 있다는 가정
        val originalMyProfileEntity = makeMyProfileEntityTestData(profileId)
        myProfileDao.upsertProfile(originalMyProfileEntity)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(makeNetworkMyProfileTestData(profileId))
        })

        userRepository.getMyProfileRefresh(profileId)

        val updatedMyProfileEntity = myProfileDao.getProfileById(profileId).firstOrNull()

        assert(updatedMyProfileEntity != null)
        assertEquals(originalMyProfileEntity.profileId, updatedMyProfileEntity!!.profileId)
        assertNotEquals(originalMyProfileEntity.introduce, updatedMyProfileEntity.introduce)
        // TestData에서 introduce를 일부러 다르게 설정 (다른 데이터로 가정하기 위해)
    }

    @Test
    fun `내 프로필 조회(Local 작업) (getMyProfile())`() = runTest {

    }

    @Test
    fun `사용자 프로필 조회 (getProfile())`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkProfileTestData)
        })

        val adapter = moshi.adapter(ProfileResponse::class.java)
        val mockResponseToObject = adapter.fromJson(networkProfileTestData)!!.toDomainModel()

        val response = userRepository.getProfile("testprofileid").last()

        Assert.assertNotNull(response)
        assertEquals(mockResponseToObject, (response as Result.Success).data)
    }

    @Test
    fun `카테고리 업데이트 (updateMyCategory())`() = runTest {

        val profileId = "honggd"
        val categories = listOf(1, 2, 3)
        myProfileDao.upsertProfile(makeMyProfileEntityTestData(profileId))

        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val response =
            userRepository.updateMyCategory(
                profileId = profileId,
                categories = categories,
            ).last()

        val myProfileEntity = myProfileDao.getProfileById(profileId).firstOrNull()

        assert((response as Result.Success).data)
        assertNotNull(myProfileEntity)
        assertEquals(myProfileEntity!!.categories, categories)
    }

    @Test
    fun `계정 신고 (reportAccount())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val reportReq = ReportReq(
            reportType = ReportType.HateSpeech,
            reporterId = "honggd",
            reportId = "bad_friend",
            postId = 123L,
            commentId = 22L
        )

        val result = userRepository.reportAccount(reportReq).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `계정 차단 (blockAccount())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val blockReq = BlockReq(
            profileId = "honggd",
            blockedId = "bad_friend",
            postId = 123L
        )

        val result = userRepository.blockAccount(blockReq).last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `계정 차단 해제 (unblockAccount())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val result =
            userRepository.unblockAccount("honggd", "bad_friend").last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `본인 프로필 수정 (editMyProfile())`() = runTest {

        val profileId = "profileId(forEdit)"
        val originalMyProfileEntity = makeMyProfileEntityTestData(profileId)
        myProfileDao.upsertProfile(originalMyProfileEntity)

        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val editProfile = EditProfile(
            nickname = "updated_nickname",
            introduce = "updated_introduce",
            photoUrl = "updated_photoUrl"
        )

        val result =
            userRepository.editMyProfile(profileId, editProfile).last()

        val updatedMyProfileEntity = myProfileDao.getProfileById(profileId).firstOrNull()

        assert((result as Result.Success).data)
        assertNotNull(updatedMyProfileEntity)
        assertEquals(originalMyProfileEntity.profileId, updatedMyProfileEntity!!.profileId)
        assertEquals(updatedMyProfileEntity.nickname, editProfile.nickname)
        assertEquals(updatedMyProfileEntity.introduce, editProfile.introduce)
        assertEquals(updatedMyProfileEntity.photoUrl, editProfile.photoUrl)
    }

    @Test
    fun `팔로우 (follow())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val result = userRepository.follow("1", "2").last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `언팔로우 (unfollow())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val result = userRepository.unfollow("1", "2").last()

        assert((result as Result.Success).data)
    }

    @Test
    fun `팔로워 목록 조회 페이지네이션 (getFollowerListPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkFollowersTestData)
        })

        val expectedResponse = moshi
            .adapter(FollowerListResponse::class.java)
            .fromJson(networkFollowersTestData)!!
            .followers.toDomainModel()

        val actualResponse = userRepository.getFollowerListPagination("1", "1", 20).last()

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `팔로워 목록 조회 페이지네이션(hasNext X) (getFollowerListPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkFollowersTestDataEndOfPage)
        })

        val actualResponse = userRepository.getFollowerListPagination("1", "1", 20).last()

        assert((actualResponse as Result.Success).data.isEmpty())
    }

    @Test
    fun `팔로잉 목록 조회 페이지네이션 (getFollowingListPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkFollowingsTestData)
        })

        val expectedResponse = moshi
            .adapter(FollowingListResponse::class.java)
            .fromJson(networkFollowingsTestData)!!
            .followings.toDomainModel()

        val actualResponse = userRepository.getFollowingListPagination("1", "1", 20).last()

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `팔로잉 목록 조회 페이지네이션(hasNext X) (getFollowingListPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkFollowingsTestDataEndOfPage)
        })

        val actualResponse = userRepository.getFollowingListPagination("1", "1", 20).last()

        assert((actualResponse as Result.Success).data.isEmpty())
    }

    @Test
    fun `차단 목록 조회 페이지네이션 (getBlockListPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkBlocksTestData)
        })

        val expectedResponse = moshi
            .adapter(BlockListResponse::class.java)
            .fromJson(networkBlocksTestData)!!
            .blockList.toDomainModel()

        val actualResponse = userRepository.getBlockListPagination("1", "1", 20).last()

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `차단 목록 조회 페이지네이션(hasNext X) (getBlockListPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkBlocksTestDataEndOfPage)
        })

        val actualResponse = userRepository.getBlockListPagination("1", "1", 20).last()

        assert((actualResponse as Result.Success).data.isEmpty())
    }

    @Test
    fun `내 댓글 목록 조회 페이지네이션 (getMyCommentListPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkMyCommentsTestData)
        })

        val expectedResponse = moshi
            .adapter(MyCommentListResponse::class.java)
            .fromJson(networkMyCommentsTestData)!!
            .posts.toDomainModel()

        val actualResponse = userRepository.getMyCommentListPagination("1", "1", 20).last()

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `내 댓글 목록 조회 페이지네이션(hasNext X) (getMyCommentListPagination())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkMyCommentsTestDataEndOfPage)
        })

        val actualResponse = userRepository.getMyCommentListPagination("1", "1", 20).last()

        assert((actualResponse as Result.Success).data.isEmpty())
    }
}