package com.team.data.network.source

import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.DisplayProfileListResponse
import com.team.data.network.model.response.tag.TagListResponse
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

interface SearchNetworkDataSource {

    suspend fun searchByPost(
        searchQuery: String,
        cursor: String,
        limit: Int
    ): Result<PostListResponse, ErrorType>

    suspend fun searchByNickname(
        searchQuery: String,
        cursor: String,
        limit: Int
    ): Result<DisplayProfileListResponse, ErrorType>

    suspend fun searchByTag(
        searchQuery: String,
        cursor: String,
        limit: Int
    ): Result<TagListResponse, ErrorType>
}