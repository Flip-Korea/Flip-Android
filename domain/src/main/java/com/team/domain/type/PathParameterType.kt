package com.team.domain.type

sealed interface PathParameterType {
    enum class Post: PathParameterType {
        CATEGORY, PROFILE, TAG
    }


}

fun PathParameterType.asString(): String =
    when (this) {
        PathParameterType.Post.CATEGORY -> { "category" }
        PathParameterType.Post.PROFILE -> { "profile" }
        PathParameterType.Post.TAG -> { "tag" }
    }