package com.team.domain.model.profile

data class BlockProfile(
    val blockId: Long,
    val blockedId: String,
    val nickname: String,
    val photoUrl: String
)
