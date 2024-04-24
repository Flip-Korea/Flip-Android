package com.team.data.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultIdResponse(
    @Json(name = "result_id") val resultId: Long
)
