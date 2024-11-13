package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.category.Category

@JsonClass(generateAdapter = true)
data class CategoryRequest(
    @Json(name = "categoryIds")
    val categoryIds: List<Int>
)

fun List<Category>.toNetwork(): CategoryRequest =
    CategoryRequest(categoryIds = this.map { it.id })