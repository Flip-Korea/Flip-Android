package com.team.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team.domain.model.category.Category

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
)

fun CategoryEntity.toDomainModel(): Category =
    Category(id, name)

fun List<CategoryEntity>.toDomainModel(): List<Category> = this.map { it.toDomainModel() }