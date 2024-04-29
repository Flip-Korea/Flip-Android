package com.team.data.network.source

import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.DisplayProfileListResponse
import com.team.data.network.model.response.tag.TagListResponse
import com.team.data.network.networkCall
import com.team.data.network.retrofit.api.SearchNetworkApi
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class SearchNetworkDataSourceImpl(
    private val searchNetworkApi: SearchNetworkApi
): SearchNetworkDataSource {

    override suspend fun searchByPost(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Result<PostListResponse, ErrorType> =
        networkCall { searchNetworkApi.searchByPost(searchQuery, cursor, limit) }

    override suspend fun searchByNickname(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Result<DisplayProfileListResponse, ErrorType> =
        networkCall { searchNetworkApi.searchByNickname(searchQuery, cursor, limit) }

    override suspend fun searchByTag(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Result<TagListResponse, ErrorType> =
        networkCall { searchNetworkApi.searchByTag(searchQuery, cursor, limit) }
}