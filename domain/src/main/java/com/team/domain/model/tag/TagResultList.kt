package com.team.domain.model.tag

data class TagResultList(
    val searchTag: String,
    val hasNext: Boolean,
    val nextCursor: String,
    val tags: List<TagResult>
)
