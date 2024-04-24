package com.team.domain.repository

import com.team.domain.model.post.NewPost
import com.team.domain.model.post.Post
import com.team.domain.type.PathParameterType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    /** 1. Data is retrieved only from local DB
     *
     * 2. This Function is for Pagination **/
    fun getPosts(): Flow<List<Post>>

    /** 1. 카테고리(전체) 및 플립(숏폼부분) 에 해당
     * 2. 로컬DB에서 가져오는 getPosts()를 통해서만 데이터를 가져옴 **/
    fun getPostsPagination(cursor: String): Flow<Result<Boolean, ErrorType>>

    /** 예외 / 오류 / 존재하지 않은 데이터 읽기 상황에서 Null 반환 **/
    fun getPostById(postId: Long): Flow<Result<Post?, ErrorType>>

    fun addPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>>

    fun editPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>>

    fun getPostsByTypePagination(
        type: PathParameterType,
        typeId: String,
        cursor: String,
    ): Flow<Result<List<Post>, ErrorType>>

    fun deletePost(postId: Long): Flow<Result<Boolean, ErrorType>>

    fun getPostsByPopularUserPagination(
        categoryId: Int,
        cursor: String,
        limit: Int
    ): Flow<Result<List<Post>, ErrorType>>

    fun likePost(profileId: String, postId: Int): Flow<Result<Boolean, ErrorType>>

    fun unLikePost(profileId: String, postId: Int): Flow<Result<Boolean, ErrorType>>
}