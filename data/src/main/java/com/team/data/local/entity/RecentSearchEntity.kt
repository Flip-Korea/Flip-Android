package com.team.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team.domain.model.RecentSearch

@Entity
data class RecentSearchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val word: String
)

fun RecentSearchEntity.toExternal(): RecentSearch = RecentSearch(id, word)

fun List<RecentSearchEntity>.toExternal(): List<RecentSearch> =
    this.map { it.toExternal() }