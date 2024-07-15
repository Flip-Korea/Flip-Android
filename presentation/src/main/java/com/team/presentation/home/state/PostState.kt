package com.team.presentation.home.state

import com.team.domain.model.post.Post
import com.team.presentation.util.UiText

data class PostState(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: UiText = UiText.DynamicString(""),
)
