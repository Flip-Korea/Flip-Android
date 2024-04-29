package com.team.data.network.model.response.tag

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.tag.TagResult

@JsonClass(generateAdapter = true)
data class TagResultResponse(
    @Json(name = "post_cnt") val postCnt: Int,
    @Json(name = "tag_name") val tagName: String
)

fun TagResultResponse.toExternal(): TagResult = TagResult(postCnt, tagName)

fun List<TagResultResponse>.toExternal(): List<TagResult> = this.map { it.toExternal() }