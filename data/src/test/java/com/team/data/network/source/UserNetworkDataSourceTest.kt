package com.team.data.network.source

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.request.BlockRequest
import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.EditProfileRequest
import com.team.data.network.model.request.FollowRequest
import com.team.data.network.model.request.ReportRequest
import com.team.data.network.model.request.ScrapCommentRequest
import com.team.data.network.model.request.ScrapRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.block.BlockListResponse
import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.model.response.comment.MyCommentListResponse
import com.team.data.network.model.response.follow.FollowerListResponse
import com.team.data.network.model.response.follow.FollowingListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.ProfileResponse
import com.team.data.network.retrofit.api.UserNetworkApi
import com.team.data.network.source.fake.FakeUserNetworkDataSource
import com.team.data.network.testdoubles.addScrapRequestTestData
import com.team.data.network.testdoubles.networkBlocksTestData
import com.team.data.network.testdoubles.networkCategoriesTestData
import com.team.data.network.testdoubles.networkFollowersTestData
import com.team.data.network.testdoubles.networkFollowingsTestData
import com.team.data.network.testdoubles.networkMyCommentsTestData
import com.team.data.network.testdoubles.networkProfileTestData
import com.team.data.network.testdoubles.postsResponseTestDataWithScrapComment
import com.team.data.network.testdoubles.resultIdResponseTestData
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
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
class UserNetworkDataSourceTest {

    private lateinit var userNetworkApi: UserNetworkApi
    private lateinit var userNetworkDataSource: UserNetworkDataSource
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
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

        userNetworkDataSource = FakeUserNetworkDataSource(userNetworkApi)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `사용자 프로필 조회 (getProfile())`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkProfileTestData)
        })

        val adapter = moshi.adapter(ProfileResponse::class.java)
        val mockResponseToObject = adapter.fromJson(networkProfileTestData)

        val response = userNetworkDataSource.getProfile("testprofileid")

        assertNotNull(response)
        assertEquals(mockResponseToObject, (response as Result.Success).data)
    }

    @Test
    fun `카테고리 관심분야 선택(selectMyCategory())`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val response =
            userNetworkDataSource.selectMyCategory(
                profileId = "honggd",
                category = CategoryRequest(listOf(1, 2, 3))
            )

        val recordedRequest = server.takeRequest()

        val adapter = moshi.adapter(CategoryRequest::class.java)
        val realRequestBody = adapter.fromJson(recordedRequest.body.peek())
        val requestBody = """
            {
                "categories": [1,2,3]
            }
        """.trimIndent()
        val expectedRequestBody = adapter.fromJson(requestBody)

        assertNotNull(response)
        assertEquals(expectedRequestBody!!.categoryIds, realRequestBody!!.categoryIds)
    }

    @Test
    fun `스크랩 목록 불러오기 (getScrapList())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(postsResponseTestDataWithScrapComment)
        })

        val expectedResponse = moshi
            .adapter(PostListResponse::class.java)
            .fromJson(postsResponseTestDataWithScrapComment)

        val actualResponse =
            userNetworkDataSource.getScrapList(profileId = "honggd", cursor = "test-cursor", limit = 15)

        assertEquals(expectedResponse!!, (actualResponse as Result.Success).data)
    }

    @Test
    fun `스크랩 코멘트 수정 (editScrapComment())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val scrapCommentRequest = ScrapCommentRequest("테스트 스크랩 코멘트")
        val result =
            userNetworkDataSource.editScrapComment(
                profileId = "TestProfileId",
                scrapId = 1L,
                scrapCommentRequest = scrapCommentRequest
            )

        assert((result as Result.Success).data)
    }

    @Test
    fun `스크랩 추가 (addScrap())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(resultIdResponseTestData)
        })

        val requestAdapter = moshi.adapter(ScrapRequest::class.java)
        val responseAdapter = moshi.adapter(ResultIdResponse::class.java)

        val actualResponse =
            userNetworkDataSource.addScrap(
                requestAdapter.fromJson(addScrapRequestTestData)!!
            )
        val expectedResponse = responseAdapter.fromJson(resultIdResponseTestData)

        Assert.assertNotNull(actualResponse)
        assertEquals(expectedResponse!!.resultId, (actualResponse as Result.Success).data.resultId)
    }

    @Test
    fun `스크랩 삭제 (deleteScrap())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val actualResponse = userNetworkDataSource.deleteScrap(1)

        Assert.assertNotNull(actualResponse)
        assertEquals(true, (actualResponse as Result.Success).data)
    }

    @Test
    fun `계정 신고 (reportAccount())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val reportRequest = ReportRequest(
            blameType = "혐오발언",
            reporterId = "honggd",
            reportId = "bad_friend",
            postId = 123L,
            commentId = 22L
        )

        val result = userNetworkDataSource.reportAccount(reportRequest)

        assert((result as Result.Success).data)
    }

    @Test
    fun `계정 차단 (blockAccount())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val blockRequest = BlockRequest(
            profileId = "honggd",
            blockedId = "bad_friend",
            postId = 123L
        )

        val result = userNetworkDataSource.blockAccount(blockRequest)

        assert((result as Result.Success).data)
    }

    @Test
    fun `계정 차단 해제 (unblockAccount())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val result =
            userNetworkDataSource.unblockAccount("honggd", "bad_friend")

        assert((result as Result.Success).data)
    }

    @Test
    fun `본인 프로필 수정 (editMyProfile())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val editProfileRequest = EditProfileRequest(
            nickname = "nickname",
            introduce = "introduce",
            photoUrl = "asd"
        )

        val result = userNetworkDataSource.editMyProfile("profileId", editProfileRequest)

        assert((result as Result.Success).data)
    }

    @Test
    fun `팔로우 (follow())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val followRequest = FollowRequest("1", "2")

        val result = userNetworkDataSource.follow(followRequest)

        assert((result as Result.Success).data)
    }

    @Test
    fun `언팔로우 (unfollow())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val followRequest = FollowRequest("1", "2")

        val result = userNetworkDataSource.unfollow(followRequest)

        assert((result as Result.Success).data)
    }

    @Test
    fun `팔로워 목록 조회 (getFollowerList())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkFollowersTestData)
        })

        val expectedResponse = moshi
            .adapter(FollowerListResponse::class.java)
            .fromJson(networkFollowersTestData)

        val actualResponse = userNetworkDataSource.getFollowerList("1", "1", 20)

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `팔로잉 목록 조회 (getFollowingList())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkFollowingsTestData)
        })

        val expectedResponse = moshi
            .adapter(FollowingListResponse::class.java)
            .fromJson(networkFollowingsTestData)

        val actualResponse = userNetworkDataSource.getFollowingList("1", "1", 20)

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `차단 목록 조회 (getBlockList())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkBlocksTestData)
        })

        val expectedResponse = moshi
            .adapter(BlockListResponse::class.java)
            .fromJson(networkBlocksTestData)

        val actualResponse = userNetworkDataSource.getBlockList("1", "1", 20)

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `내 댓글 목록 조회 (getMyCommentList())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkMyCommentsTestData)
        })

        val expectedResponse = moshi
            .adapter(MyCommentListResponse::class.java)
            .fromJson(networkMyCommentsTestData)

        val actualResponse = userNetworkDataSource.getMyCommentList("1", "1", 20)

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `내 카테고리 관심분야 조회 (getMyCategories())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkCategoriesTestData)
        })

        val listType = Types.newParameterizedType(List::class.java, CategoryResponse::class.java)
        val adapter: JsonAdapter<List<CategoryResponse>> = moshi.adapter(listType)
        val expectedResponse = adapter.fromJson(networkCategoriesTestData)

        val actualResponse = userNetworkDataSource.getMyCategories("1")

        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }
}