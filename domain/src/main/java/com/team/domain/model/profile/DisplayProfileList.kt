package com.team.domain.model.profile

/** 다른 사용자의 프로필 일부분
 *
 * 플립 글에서 보여지는 프로필 일부**/
data class DisplayProfileList(
    val hasNext: Boolean,
    val nextCursor: String,
    val displayProfileList: List<DisplayProfile>
)
