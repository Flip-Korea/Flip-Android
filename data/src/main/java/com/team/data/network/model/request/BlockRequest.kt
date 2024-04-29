package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.report_block.BlockReq

@JsonClass(generateAdapter = true)
data class BlockRequest(
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "post_id") val postId: Long?,
    @Json(name = "blocked_id") val blockedId: String,
)

fun BlockReq.toNetwork(): BlockRequest =
    BlockRequest(profileId, postId, blockedId)