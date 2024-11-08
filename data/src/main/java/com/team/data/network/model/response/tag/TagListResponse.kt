package com.team.data.network.model.response.tag

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.tag.TagResultList

@JsonClass(generateAdapter = true)
data class TagListResponse(
    @Json(name = "search_tag") val searchTag: String,
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "next_cursor") val nextCursor: String,
    @Json(name = "tags") val tags: List<TagResultResponse>
)

fun TagListResponse.toDomainModel(): TagResultList =
    TagResultList(searchTag, hasNext, nextCursor, tags.toDomainModel())