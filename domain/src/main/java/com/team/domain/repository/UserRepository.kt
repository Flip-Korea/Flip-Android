package com.team.domain.repository

import com.team.domain.model.category.Category
import com.team.domain.model.post.DisplayPost
import com.team.domain.model.post.Post
import com.team.domain.model.profile.BlockProfile
import com.team.domain.model.profile.DisplayProfile
import com.team.domain.model.profile.EditProfile
import com.team.domain.model.profile.MyProfile
import com.team.domain.model.profile.Profile
import com.team.domain.model.report_block.BlockReq
import com.team.domain.model.report_block.ReportReq
import com.team.domain.model.scrap.NewScrap
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getMyProfile(profileId: String): Flow<Result<MyProfile?, ErrorType>>

    fun getProfile(profileId: String): Flow<Result<Profile, ErrorType>>

    fun updateMyCategory(
        profileId: String,
        categories: List<Int>
    ): Flow<Result<Boolean, ErrorType>>

    fun reportAccount(reportReq: ReportReq): Flow<Result<Boolean, ErrorType>>

    fun blockAccount(blockReq: BlockReq): Flow<Result<Boolean, ErrorType>>

    fun unblockAccount(
        profileId: String,
        blockedId: String
    ): Flow<Result<Boolean, ErrorType>>

    fun editMyProfile(
        profileId: String,
        editProfile: EditProfile
    ): Flow<Result<Boolean, ErrorType>>

    /** 1. followingID: 나의 profile ID
        2. followerId: 팔로우 당한 profile ID**/
    fun follow(
        followingId: String,
        followerId: String
    ): Flow<Result<Boolean, ErrorType>>

    /** 1. followingID: 나의 profile ID
    2. followerId: 팔로우 당한 profile ID**/
    fun unfollow(
        followingId: String,
        followerId: String
    ): Flow<Result<Boolean, ErrorType>>

    fun getFollowerListPagination(
        profileId: String,
        cursor: String,
        limit: Int
    ): Flow<Result<List<DisplayProfile>, ErrorType>>

    fun getFollowingListPagination(
        profileId: String,
        cursor: String,
        limit: Int
    ): Flow<Result<List<DisplayProfile>, ErrorType>>

    fun getBlockListPagination(
        profileId: String,
        cursor: String,
        limit: Int
    ): Flow<Result<List<BlockProfile>, ErrorType>>

    fun getMyCommentListPagination(
        profileId: String,
        cursor: String,
        limit: Int
    ): Flow<Result<List<DisplayPost>, ErrorType>>
}