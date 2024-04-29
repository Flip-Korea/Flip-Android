package com.team.data.network.model.response.category

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryResponseWrapper(
    @Json(name = "categories")
    val categories: List<CategoryItemResponse>
)

@JsonClass(generateAdapter = true)
data class CategoryItemResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "icon") val icon: String,
)