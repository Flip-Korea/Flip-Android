package com.team.data.network.model.response.block

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.profile.BlockProfileList

@JsonClass(generateAdapter = true)
data class BlockListResponse(
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "next_cursor") val nextCursor: String,
    @Json(name = "block_list") val blockList: List<BlockProfileResponse>,
)

fun BlockListResponse.toDomainModel(): BlockProfileList =
    BlockProfileList(hasNext, nextCursor, blockList.toDomainModel())