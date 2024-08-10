package com.team.domain.usecase.post.testdoubles

import com.team.domain.model.post.NewPost
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType
import com.team.domain.type.asString

val newPostTestData = NewPost(
    title = "title",
    content = "111\nnext_page\n222",
    bgColorType = BackgroundColorType.DEFAULT.asString(),
    fontStyleType = FontStyleType.DEFAULT.asString(),
    tags = emptyList(),
    categoryId = 2
)