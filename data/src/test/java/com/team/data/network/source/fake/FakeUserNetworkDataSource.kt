package com.team.data.network.source.fake

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.ScrapCommentRequest
import com.team.data.network.model.request.ScrapRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.post.PostResponse
import com.team.data.network.model.response.profile.DisplayProfileResponse
import com.team.data.network.model.response.profile.ProfileResponse
import com.team.data.network.retrofit.api.UserNetworkApi
import com.team.data.network.source.UserNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlin.random.Random

class FakeUserNetworkDataSource(
    private val userNetworkApi: UserNetworkApi,
) : UserNetworkDataSource {

    /** 총 3페이지만 반환 **/
    private fun makeScrapListResponseTestData(
        profileId: String,
        cursor: String,
        pageSize: Int,
    ): PostListResponse {

        val postIds = mutableListOf<Long>()
        repeat(pageSize) {
            postIds.add(Random.nextLong(1, 50000))
        }
        val list = mutableListOf<PostResponse>()

        val hasNext = cursor != "4"
        val nextCursor = (cursor.toInt()+1).toString()

        postIds.forEachIndexed{ index, postId ->
            list.add(
                PostResponse(
                    postId = postId,
                    profile = DisplayProfileResponse(
                        profileId = profileId,
                        nickname = "nickname",
                        photoUrl = "https://test.com/123",
                        following = false,
                        followerCnt = null
                    ),
                    title = "TestTitle($index)",
                    content = "TestContent($index)",
                    liked = false,
                    likeCnt = 10,
                    commentCnt = 10,
                    scrapCnt = 10,
                    scraped = false,
                    scrapComment = "My Scrap",
                    categoryId = 1,
                    bgColorId = 1,
                    fontStyleId = 1,
                    tag = listOf("1","2"),
                    createdAt = cursor
                )
            )
        }

        return PostListResponse(
            hasNext = hasNext,
            nextCursor = nextCursor,
            postCnt = pageSize.toLong(),
            posts = list
        )
    }

    override suspend fun getProfile(profileId: String): Result<ProfileResponse, ErrorType> {
//            emit(NetworkResult.Loading) // -> 테스트 시 오류 발생 (코루틴이 제대로 수집 되지 않아서 인듯)
        val result = userNetworkApi.getProfile(profileId)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun selectMyCategory(
        profileId: String,
        category: CategoryRequest,
    ): Result<Boolean, ErrorType> {
        val result = userNetworkApi.selectMyCategory(profileId, category)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun updateMyCategory(
        profileId: String,
        category: CategoryRequest,
    ): Result<Boolean, ErrorType> {
        val result = userNetworkApi.updateMyCategory(profileId, category)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getScrapList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<PostListResponse, ErrorType> {
//        val result = userNetworkApi.getScrapList(profileId, cursor, limit)
        val scrapList = makeScrapListResponseTestData(profileId, cursor, limit)
        return Result.Success(scrapList)
    }

    override suspend fun editScrapComment(
        profileId: String,
        scrapId: Long,
        scrapCommentRequest: ScrapCommentRequest,
    ): Result<Boolean, ErrorType> {
        val result = userNetworkApi.editScrapComment(profileId, scrapId, scrapCommentRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun addScrap(scrapRequest: ScrapRequest): Result<ResultIdResponse, ErrorType> {
        val result = userNetworkApi.addScrap(scrapRequest)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun deleteScrap(scrapId: Long): Result<Boolean, ErrorType> {
        val result = userNetworkApi.deleteScrap(scrapId)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }
}