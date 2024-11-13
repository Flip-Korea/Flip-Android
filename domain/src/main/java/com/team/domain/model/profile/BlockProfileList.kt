package com.team.domain.model.profile

data class BlockProfileList(
    val hasNext: Boolean,
    val nextCursor: String,
    val blockProfileList: List<BlockProfile>
)
