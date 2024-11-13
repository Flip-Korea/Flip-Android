package com.team.data.network.model.response.post

import com.team.domain.type.BackgroundColorType

enum class BackgroundColorTypeResponse {
    DEFAULT,
    RED,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE
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