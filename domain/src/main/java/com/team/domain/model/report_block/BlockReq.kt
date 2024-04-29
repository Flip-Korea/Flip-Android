package com.team.domain.model.report_block

data class BlockReq(
    val profileId: String,
    val postId: Long?,
    val blockedId: String
)
