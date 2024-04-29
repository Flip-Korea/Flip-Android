package com.team.data.network.model.response.block

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.profile.BlockProfile

@JsonClass(generateAdapter = true)
data class BlockProfileResponse(
    @Json(name = "block_id") val blockId: Long,
    @Json(name = "blocked_id") val blockedId: String,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "photo_url") val photoUrl: String
)

fun BlockProfileResponse.toExternal(): BlockProfile =
    BlockProfile(blockId, blockedId, nickname, photoUrl)

fun List<BlockProfileResponse>.toExternal(): List<BlockProfile> =
    this.map { it.toExternal() }