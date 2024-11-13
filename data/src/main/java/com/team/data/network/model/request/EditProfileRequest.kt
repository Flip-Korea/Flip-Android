package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.profile.EditProfile

@JsonClass(generateAdapter = true)
data class EditProfileRequest(
    @Json(name = "nickname")
    val nickname: String?,
    @Json(name = "introduce")
    val introduce: String?,
    @Json(name = "photo_url")
    val photoUrl: String?,
)

fun EditProfile.toNetwork(): EditProfileRequest =
    EditProfileRequest(nickname, introduce, photoUrl)