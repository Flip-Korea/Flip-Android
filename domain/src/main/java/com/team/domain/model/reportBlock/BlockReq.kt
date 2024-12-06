package com.team.domain.model.reportBlock

data class BlockReq(
    val profileId: String,
    val postId: Long?,
    val blockedId: String,
)
