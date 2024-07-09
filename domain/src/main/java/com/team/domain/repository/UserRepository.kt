package com.team.domain.repository

import com.team.domain.model.post.DisplayPostList
import com.team.domain.model.profile.BlockProfileList
import com.team.domain.model.profile.DisplayProfileList
import com.team.domain.model.profile.EditProfile
import com.team.domain.model.profile.MyProfile
import com.team.domain.model.profile.Profile
import com.team.domain.model.report_block.BlockReq
import com.team.domain.model.report_block.ReportReq
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    /**
     * 나의 프로필을 Local DB 에서 가져온다.
     *
     * 1. Data is retrieved only from local DB
     *
     * @param profileId 나의 프로필 ID
     */
    fun getMyProfileFromLocal(profileId: String): Flow<Result<MyProfile?, ErrorType>>

    /**
     * 나의 프로필을 Network 를 통해 업데이트 한다.
     *
     * Network 에서 데이터를 가져와서 Local DB 에 저장한다.
     *
     * @param profileId 나의 프로필 ID
     * @return X
     */
    suspend fun refreshMyProfile(profileId: String)

    /**
     * (나를 제외한) 사용자의 프로필을 가져온다.
     *
     * @param profileId 사용자의 프로필 ID
     */
    fun getProfile(profileId: String): Flow<Result<Profile, ErrorType>>

    /**
     * 나의 관심 카테고리 업데이트
     *
     * Local/Network 간에 데이터 동기화가 이뤄진다.
     *
     * @param categoryIds 수정한 카테고리 리스트
     */
    fun updateMyCategories(
        categoryIds: List<Int>
    ): Flow<Result<Boolean, ErrorType>>

    /**
     * 계정 신고
     *
     * @param reportReq 계정 신고 요청 폼
     */
    fun reportAccount(reportReq: ReportReq): Flow<Result<Boolean, ErrorType>>

    /**
     * 계정 차단
     *
     * @param blockReq 계정 차단 요청 폼
     */
    fun blockAccount(blockReq: BlockReq): Flow<Result<Boolean, ErrorType>>

    /**
     * 계정 차단 해제
     *
     * @param profileId 나의 프로필 ID
     * @param blockedId 차단 해제할 프로필 ID
     */
    fun unblockAccount(
        profileId: String,
        blockedId: String
    ): Flow<Result<Boolean, ErrorType>>

    /**
     * 나의 프로필 수정
     *
     * Local/Network 간에 데이터 동기화가 이뤄진다.
     *
     * @param profileId 나의 프로필 ID
     * @param editProfile 수정된 프로필 데이터
     */
    fun editMyProfile(
        profileId: String,
        editProfile: EditProfile
    ): Flow<Result<Boolean, ErrorType>>

    /**
     * 팔로우
     *
     * @param followingId: 나의 profile ID
     * @param followerId: 팔로우 당한 profile ID
     */
    fun follow(
        followingId: String,
        followerId: String
    ): Flow<Result<Boolean, ErrorType>>

    /**
     * 언팔로우
     *
     * @param followingId: 나의 profile ID
     * @param followerId: 팔로우 당한 profile ID
     */
    fun unfollow(
        followingId: String,
        followerId: String
    ): Flow<Result<Boolean, ErrorType>>

    /**
     * 팔로워 리스트 페이지네이션
     *
     * @param profileId 프로필 ID
     * @param cursor nextCursor 즉, 다음 커서로 요청하여 다음 페이지를 불러온다.
     * @param limit 1 페이지 당 불러올 개수
     */
    fun getFollowerListPagination(
        profileId: String,
        cursor: String,
        limit: Int
    ): Flow<Result<DisplayProfileList, ErrorType>>

    /**
     * 팔로잉 리스트 페이지네이션
     *
     * @param profileId 프로필 ID
     * @param cursor nextCursor 즉, 다음 커서로 요청하여 다음 페이지를 불러온다.
     * @param limit 1 페이지 당 불러올 개수
     */
    fun getFollowingListPagination(
        profileId: String,
        cursor: String,
        limit: Int
    ): Flow<Result<DisplayProfileList, ErrorType>>

    /**
     * 차단 계정 리스트 페이지네이션
     *
     * @param profileId 프로필 ID
     * @param cursor nextCursor 즉, 다음 커서로 요청하여 다음 페이지를 불러온다.
     * @param limit 1 페이지 당 불러올 개수
     */
    fun getBlockListPagination(
        profileId: String,
        cursor: String,
        limit: Int
    ): Flow<Result<BlockProfileList, ErrorType>>

    /**
     * 나의 댓글 리스트 페이지네이션
     *
     * @param profileId 프로필 ID
     * @param cursor nextCursor 즉, 다음 커서로 요청하여 다음 페이지를 불러온다.
     * @param limit 1 페이지 당 불러올 개수
     */
    fun getMyCommentListPagination(
        profileId: String,
        cursor: String,
        limit: Int
    ): Flow<Result<DisplayPostList, ErrorType>>
}