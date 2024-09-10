package com.team.domain.usecase.post.testdoubles

import com.team.domain.model.post.NewPost
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType

val newPostTestData = NewPost(
    title = "title",
    content = "111\nnext_page\n222",
    bgColorType = BackgroundColorType.DEFAULT,
    fontStyleType = FontStyleType.NORMAL,
    tags = emptyList(),
    categoryId = 2
)