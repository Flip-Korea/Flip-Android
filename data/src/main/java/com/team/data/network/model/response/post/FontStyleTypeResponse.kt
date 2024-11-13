package com.team.data.network.model.response.post

import com.team.domain.type.FontStyleType

enum class FontStyleTypeResponse {
    NORMAL, BOLD
}

fun FontStyleTypeResponse.toDomainModel(): FontStyleType =
    when (this) {
        FontStyleTypeResponse.NORMAL -> FontStyleType.NORMAL
        FontStyleTypeResponse.BOLD -> FontStyleType.BOLD
    }