package com.team.data.network.model.response.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.data.local.entity.profile.DisplayProfileEntity
import com.team.domain.model.profile.DisplayProfile

@JsonClass(generateAdapter = true)
data class DisplayProfileResponse(
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "photo_url") val photoUrl: String,
    @Json(name = "is_follower") val isFollower: Boolean,
    @Json(name = "is_following") val isFollowing: Boolean,
    @Json(name = "introduce") val introduce: String?,
    @Json(name = "follower_cnt") val followerCnt: Long?,
    @Json(name = "rating") val rating: String?,
)

fun DisplayProfileResponse.toEntity(): DisplayProfileEntity =
    DisplayProfileEntity(profileId, nickname, photoUrl, isFollower, isFollowing, introduce, followerCnt, rating)

fun DisplayProfileResponse.toDomainModel(): DisplayProfile =
    DisplayProfile(profileId, nickname, photoUrl, isFollower, isFollowing, introduce, followerCnt, rating)

fun List<DisplayProfileResponse>.toDomainModel(): List<DisplayProfile> =
    this.map { it.toDomainModel() }