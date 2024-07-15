package com.team.domain.repository

import com.team.domain.model.post.NewPost
import com.team.domain.model.post.Post
import com.team.domain.model.post.PostList
import com.team.domain.type.PathParameterType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    /**
     * 캐시를 위해 Post(Flip) 리스트를 Local DB 에서 가져 온다.
     *
     * 1. Data is retrieved only from local DB
     */
    fun getCachedPosts(): Flow<List<Post>>

    /**
     * Post(Flip) 리스트를 네트워크에서 페이지네이션을 통해 가져온다.
     *
     * 1. 카테고리(전체) 및 플립(숏폼부분) 에 해당
     * 2. Post 를 가져올 땐 최대한 getPostsFromLocal()를 통해서 로컬 DB 에서 가져옴
     *
     * @param cursor nextCursor 즉, 다음 커서로 요청하여 다음 페이지를 불러온다.
     * @param limit 1 페이지 당 불러올 개수
     */
    fun getPostsPagination(cursor: String?, limit: Int): Flow<Result<PostList, ErrorType>>

    /**
     * ID 로 Post(Flip)를 가져온다.
     *
     * 1. 예외 / 오류 / 존재하지 않은 데이터 읽기 상황에서 Null 반환
     *
     * @param postId Post ID
     * @return Post 는 Nullable 타입
     */
    fun getPostById(postId: Long): Flow<Result<Post?, ErrorType>>

    /**
     * Post(Flip)를 저장하고 Boolean 타입의 결과를 받는다.
     *
     * @param newPost 새로운 Post를 저장할 때 사용되는 Model
     */
    fun addPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>>

    /**
     * Post(Flip)를 수정하고 Boolean 타입의 결과를 받는다.
     *
     * @param newPost 새로운 Post를 저장할 때 사용되는 Model과 같음
     */
    fun editPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>>

    /**
     * Post(Flip) 리스트를 타입 별로 페이지네이션을 통해 가져온다.
     *
     * @param type 카테고리, 프로필, 태그 등
     * @param typeId type 에 해당하는 ID
     * @param cursor nextCursor 즉, 다음 커서로 요청하여 다음 페이지를 불러온다.
     * @param limit 1 페이지 당 불러올 개수
     */
    fun getPostsByTypePagination(
        type: PathParameterType,
        typeId: String,
        cursor: String?,
        limit: Int
    ): Flow<Result<PostList, ErrorType>>

    /**
     * Post(Flip)를 삭제하고 Boolean 타입의 결과를 받는다.
     *
     * @param postId Post ID
     */
    fun deletePost(postId: Long): Flow<Result<Boolean, ErrorType>>

    /**
     * 인기있는 Post(Flip)리스트를 페이지네이션을 통해 가져온다.
     *
     * @param categoryId Category ID
     * @param cursor nextCursor 즉, 다음 커서로 요청하여 다음 페이지를 불러온다.
     * @param limit 1 페이지 당 불러올 개수
     */
    fun getPostsByPopularUserPagination(
        categoryId: Int,
        cursor: String?,
        limit: Int
    ): Flow<Result<PostList, ErrorType>>

    /**
     * Post(Flip) 좋아요 요청
     *
     * @param profileId 본인의 Profile ID
     * @param postId 좋아요를 누른 Post ID
     */
    fun likePost(profileId: String, postId: Int): Flow<Result<Boolean, ErrorType>>

    /**
     * Post(Flip) 좋아요 취소 요청
     *
     * @param profileId 본인의 Profile ID
     * @param postId 좋아요를 누른 Post ID
     */
    fun unLikePost(profileId: String, postId: Int): Flow<Result<Boolean, ErrorType>>
}