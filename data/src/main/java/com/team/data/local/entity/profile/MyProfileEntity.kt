package com.team.data.local.entity.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team.domain.model.profile.MyProfile

/** This Entity is My Profile Entity **/
@Entity
data class MyProfileEntity(
    @PrimaryKey(autoGenerate = false)
    val profileId: String,
    val nickname: String,
    val introduce: String,
    val photoUrl: String,
    val postCnt: Int,
    val followerCnt: Int,
    val followingCnt: Int,
    val categories: List<Int>,
    val rating: String
)

fun List<MyProfileEntity>.toExternal(): List<MyProfile> =
    this.map { it.toExternal() }

fun MyProfileEntity.toExternal(): MyProfile =
    MyProfile(
        profileId = profileId,
        nickname = nickname,
        introduce = introduce,
        photoUrl = photoUrl,
        postCnt = postCnt,
        followerCnt = followerCnt,
        followingCnt = followingCnt,
        categories = categories,
        rating = rating,
    )