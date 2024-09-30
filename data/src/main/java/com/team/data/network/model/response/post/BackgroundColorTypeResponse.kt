package com.team.data.network.model.response.post

import com.team.data.local.entity.post.BackgroundColorTypeEntity
import com.team.domain.type.BackgroundColorType

enum class BackgroundColorTypeResponse {
    DEFAULT,
    RED,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE
}

fun BackgroundColorTypeResponse.toEntity(): BackgroundColorTypeEntity =
    when (this) {
        BackgroundColorTypeResponse.DEFAULT -> BackgroundColorTypeEntity.DEFAULT
        BackgroundColorTypeResponse.RED -> BackgroundColorTypeEntity.RED
        BackgroundColorTypeResponse.YELLOW -> BackgroundColorTypeEntity.YELLOW
        BackgroundColorTypeResponse.GREEN -> BackgroundColorTypeEntity.GREEN
        BackgroundColorTypeResponse.BLUE -> BackgroundColorTypeEntity.BLUE
        BackgroundColorTypeResponse.PURPLE -> BackgroundColorTypeEntity.PURPLE
    }

fun BackgroundColorTypeResponse.toDomainModel(): BackgroundColorType =
    when (this) {
        BackgroundColorTypeResponse.DEFAULT -> BackgroundColorType.DEFAULT
        BackgroundColorTypeResponse.RED -> BackgroundColorType.RED
        BackgroundColorTypeResponse.YELLOW -> BackgroundColorType.YELLOW
        BackgroundColorTypeResponse.GREEN -> BackgroundColorType.GREEN
        BackgroundColorTypeResponse.BLUE -> BackgroundColorType.BLUE
        BackgroundColorTypeResponse.PURPLE -> BackgroundColorType.PURPLE
    }