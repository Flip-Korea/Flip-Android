package com.team.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team.domain.model.RecentSearch

@Entity
data class RecentSearchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val word: String
)

fun RecentSearchEntity.toDomainModel(): RecentSearch = RecentSearch(id, word)

fun List<RecentSearchEntity>.toDomainModel(): List<RecentSearch> =
    this.map { it.toDomainModel() }