package com.team.data.local.entity.post

import com.team.domain.type.BackgroundColorType

enum class BackgroundColorTypeEntity {
    DEFAULT,
    RED,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE
}

fun BackgroundColorTypeEntity.toDomainModel(): BackgroundColorType =
    when (this) {
        BackgroundColorTypeEntity.DEFAULT -> BackgroundColorType.DEFAULT
        BackgroundColorTypeEntity.RED -> BackgroundColorType.RED
        BackgroundColorTypeEntity.YELLOW -> BackgroundColorType.YELLOW
        BackgroundColorTypeEntity.GREEN -> BackgroundColorType.GREEN
        BackgroundColorTypeEntity.BLUE -> BackgroundColorType.BLUE
        BackgroundColorTypeEntity.PURPLE -> BackgroundColorType.PURPLE
    }

fun BackgroundColorType.toEntity(): BackgroundColorTypeEntity =
    when (this) {
        BackgroundColorType.DEFAULT -> BackgroundColorTypeEntity.DEFAULT
        BackgroundColorType.RED -> BackgroundColorTypeEntity.RED
        BackgroundColorType.YELLOW -> BackgroundColorTypeEntity.YELLOW
        BackgroundColorType.GREEN -> BackgroundColorTypeEntity.GREEN
        BackgroundColorType.BLUE -> BackgroundColorTypeEntity.BLUE
        BackgroundColorType.PURPLE -> BackgroundColorTypeEntity.PURPLE
    }