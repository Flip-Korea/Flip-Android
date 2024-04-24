package com.team.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team.domain.model.category.Category

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val icon: Int?,
)

fun CategoryEntity.toExternal(): Category =
    Category(id, name, icon)

fun List<CategoryEntity>.toExternal(): List<Category> = this.map { it.toExternal() }