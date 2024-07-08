package com.team.data.network.model.response.category

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.data.local.entity.CategoryEntity
import com.team.domain.model.category.Category

@JsonClass(generateAdapter = true)
data class CategoryResponse(
    @Json(name = "categoryId") val categoryId: Int,
    @Json(name = "categoryName") val categoryName: String,
)

fun CategoryResponse.toDomainModel(): Category =
    Category(categoryId, categoryName)

fun CategoryResponse.toEntity(): CategoryEntity =
    CategoryEntity(categoryId, categoryName)