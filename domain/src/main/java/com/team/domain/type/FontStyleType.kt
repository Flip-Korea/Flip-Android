package com.team.domain.type

enum class FontStyleType {
    DEFAULT,
}

fun FontStyleType.asString(): String =
    when (this) {
        FontStyleType.DEFAULT -> "NORMAL"
    }