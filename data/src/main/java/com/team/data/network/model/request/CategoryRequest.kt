package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryRequest(
    @Json(name = "categories")
    val categories: List<Int>
)