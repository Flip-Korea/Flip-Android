package com.team.data.network.source.fake

import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.DisplayProfileListResponse
import com.team.data.network.model.response.tag.TagListResponse
import com.team.data.network.retrofit.api.SearchNetworkApi
import com.team.data.network.source.SearchNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class FakeSearchNetworkDataSource(
    private val searchNetworkApi: SearchNetworkApi
): SearchNetworkDataSource {

    override suspend fun searchByPost(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Result<PostListResponse, ErrorType> {
        val result = searchNetworkApi.searchByPost(searchQuery, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun searchByNickname(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Result<DisplayProfileListResponse, ErrorType> {
        val result = searchNetworkApi.searchByNickname(searchQuery, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun searchByTag(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Result<TagListResponse, ErrorType> {
        val result = searchNetworkApi.searchByTag(searchQuery, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }
}