package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.category.Category

@JsonClass(generateAdapter = true)
data class CategoryRequest(
    @Json(name = "categories")
    val categories: List<Int>
)

fun List<Category>.toNetwork(): CategoryRequest =
    CategoryRequest(categories = this.map { it.id })