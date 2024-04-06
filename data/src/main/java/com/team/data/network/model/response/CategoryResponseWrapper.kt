package com.team.data.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseCategoryWrapper(
    @Json(name = "categories")
    val categories: List<ResponseCategoryItem>
)

@JsonClass(generateAdapter = true)
data class ResponseCategoryItem(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "icon") val icon: String,
)