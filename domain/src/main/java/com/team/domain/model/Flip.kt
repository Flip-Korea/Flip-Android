package com.team.domain.model

data class Flip(
    val flipId: Long,
    val profile: Profile,
    val title: String,
    val content: String,
    val lookMode: Boolean,
    val likeTotal: Long,
    val commentTotal: Long,
    val categoryId: Int,
    val tagList: List<String> = emptyList(),
    val liked: Boolean,
    val scraped: Boolean,
    val scrapTotal: Long,
    val backgroundColorId: Int,
    val fontStyleId: Int,
    val createdAt: String
)
